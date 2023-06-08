package org.mint.smallcloud.label.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.file.domain.FileType;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.label.domain.DefaultLabelType;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class LabelControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private LabelRepository labelRepository;
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/labels";
    private final String DOCUMENT_NAME = "Labels/{ClassName}/{methodName}";
    private UserDetailsDto userDetailsDto;
    private Member member;
    private JwtTokenDto memberToken;
    FileType fileType;
    FileLocation fileLocation;
    Folder rootFolder;
    Label label;
    DataNode dataNode;
    DataNode dataNode1;
    DataNode dataNode2;
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        member = Member.createCommon("testMemberName", "testPw", "testNickname");
        em.persist(member);

        userDetailsDto = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();
        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);

        label = Label.of("testLabel", member);
        em.persist(label);

        fileType = FileType.of("testFileType", "testType");
        fileLocation = FileLocation.of("testLocation");

        rootFolder = Folder.createRoot(member);
        em.persist(rootFolder);
        dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        em.persist(dataNode);
        dataNode1 = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        em.persist(dataNode1);
        dataNode2 = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        em.persist(dataNode2);
        em.flush();
    }

    @Nested
    @DisplayName("/labels/ 라벨 검색 테스트")
    class search {
        private final String url = URL_PREFIX + "/search?labelName={labelName}";
        @BeforeEach
        void boot() {
            dataNode.addLabel(label);
            dataNode1.addLabel(label);
            em.persist(dataNode);
            em.persist(dataNode1);
            em.flush();
        }

        @DisplayName("정상적인 라벨 검색")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, label.getName()), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        Label label1 = labelRepository.findByNameAndOwner(label.getName(), member);
                        List<DataNode> files = label1.getFiles();
                        assertThat(files.size()).isEqualTo(2);
                        assertThat(files.contains(dataNode)).isTrue();
                        assertThat(files.contains(dataNode1)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME,  requestParameters(
                            parameterWithName("labelName").description("라벨의 이름를 담고 있습니다."))));
        }
        @DisplayName("존재하지 않는 라벨 검색")
        @Test
        void noLabel() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, "test"), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andDo(document(DOCUMENT_NAME,  requestParameters(
                            parameterWithName("labelName").description("라벨의 이름를 담고 있습니다."))));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, label.getName()), "testToken"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME,  requestParameters(
                            parameterWithName("labelName").description("라벨의 이름를 담고 있습니다."))));
        }
    }

    @Nested
    @DisplayName("/labels/trash 휴지통 라벨 검색 테스트")
    class trash {
        private final String url = URL_PREFIX + "/trash";
        Label trashLabel;
        @BeforeEach
        void boot() {
            trashLabel = Label.of(DefaultLabelType.defaultTrash.getLabelName(), member);
            em.persist(trashLabel);
            dataNode.addLabel(trashLabel);
            em.persist(dataNode);
            dataNode1.addLabel(trashLabel);
            em.persist(dataNode1);
            em.flush();
        }

        @DisplayName("정상적인 라벨 검색")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        Label label1 = labelRepository.findByNameAndOwner(trashLabel.getName(), member);
                        List<DataNode> files = label1.getFiles();
                        assertThat(files.size()).isEqualTo(2);
                        assertThat(files.contains(dataNode)).isTrue();
                        assertThat(files.contains(dataNode1)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url), "testToken"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }
    @Nested
    @DisplayName("/labels/trash 휴지통 라벨 검색 테스트")
    class favorite {
        private final String url = URL_PREFIX + "/favorite";
        Label favoritelabel;
        @BeforeEach
        void boot() {
            favoritelabel = Label.of(DefaultLabelType.defaultFavorite.getLabelName(), member);
            em.persist(favoritelabel);
            dataNode.addLabel(favoritelabel);
            em.persist(dataNode);
            dataNode1.addLabel(favoritelabel);
            em.persist(dataNode1);
            em.flush();
        }

        @DisplayName("정상적인 라벨 검색")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        Label label1 = labelRepository.findByNameAndOwner(favoritelabel.getName(), member);
                        List<DataNode> files = label1.getFiles();
                        assertThat(files.size()).isEqualTo(2);
                        assertThat(files.contains(dataNode)).isTrue();
                        assertThat(files.contains(dataNode1)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url), "testToken"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }


}