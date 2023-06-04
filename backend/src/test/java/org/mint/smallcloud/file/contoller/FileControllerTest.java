package org.mint.smallcloud.file.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.*;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.repository.FileRepository;
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
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class FileControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @PersistenceContext
    private EntityManager em;

    private MockMvc mockMvc;
    private final String URL_PREFIX = "/files";
    private final String DOCUMENT_NAME = "file/{ClassName}/{methodName}";
    private Member member;
    private UserDetailsDto userDetailsDto;
    private JwtTokenDto memberToken;
    private FileType fileType;
    private FileLocation fileLocation;
    private Folder rootFolder;
    private DataNode dataNode;
    private LabelUpdateDto labelUpdateDto;
    private LabelUpdateDto labelUpdateDto1;
    private LabelUpdateDto labelUpdateDto2;
    private List<String> labels = new ArrayList<>();
    private List<String> labels1 = new ArrayList<>();

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    FileRepository fileRepository;

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
        em.flush();

        userDetailsDto = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();

        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);

        fileType = FileType.of("testFileType", "testType");
        fileLocation = FileLocation.of("testLocation");

        rootFolder = Folder.createRoot(member);
        dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        em.persist(rootFolder);
        em.persist(dataNode);
        em.flush();
        rootFolder.addChild(dataNode);

        labels.add("testLabel1");
        labels.add("testLabel2");
        labels.add("testLabel3");

        labels1.add("testLabel1");
        labels1.add("testLabel4");

    }

    @Nested
    @DisplayName("/files/update/label docs")
    class labelUpdate {
        private final String url =  URL_PREFIX + "/update/label";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private Label label;
        private Label label1;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);
            em.flush();

            label = Label.of("testLabel1", member);
            em.persist(label);
            em.flush();

            label1 = Label.of("testLabel2", member);
            em.persist(label1);
            em.flush();

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            dataNode1.addLabel(label1);
            em.persist(dataNode);
            em.persist(dataNode1);
            em.flush();

            labelUpdateDto = LabelUpdateDto.builder()
                    .fileId(dataNode.getId())
                    .labels(labels)
                    .build();

            labelUpdateDto1 = LabelUpdateDto.builder()
                    .fileId(100L)
                    .labels(labels)
                    .build();

            labelUpdateDto2 =  LabelUpdateDto.builder()
                    .fileId(dataNode1.getId())
                    .labels(labels1)
                    .build();
        }
        @DisplayName("정상 요청(파일에 라벨이 하나도 없을 때)")
        @Test
        void okNoLabels() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                    fieldWithPath("fileId").description("파일의 id를 담고 있습니다."),
                    fieldWithPath("labels").description("파일에 등록할 라벨 리스트를 담고 있습니다."));
            mockMvc.perform(
                            TestSnippet.secured(post(url),
                                    memberToken.getAccessToken(), objectMapper, labelUpdateDto))
                    .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        List<Label> labels = labelRepository.findAll();
                        assertThat(labels.size()).isEqualTo(3);
                        assertThat(labels.get(0).getName()).isEqualTo("testLabel1");
                        assertThat(labels.get(1).getName()).isEqualTo("testLabel2");
                        assertThat(labels.get(2).getName()).isEqualTo("testLabel3");
                    })
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(labelUpdateDto.getFileId()).get();
                        assertThat(file.getLabels().size()).isEqualTo(3);
                    })
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("정상 요청(라벨 있는 파일에서 라벨 변경)")
        @Test
        void okSomeLabels() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url),
                            memberToken.getAccessToken(), objectMapper, labelUpdateDto2))
                    .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        List<Label> labels = labelRepository.findAll();
                        assertThat(labels.size()).isEqualTo(3);
                        assertThat(labels.get(0).getName()).isEqualTo("testLabel1");
                        assertThat(labels.get(1).getName()).isEqualTo("testLabel2");
                        assertThat(labels.get(2).getName()).isEqualTo("testLabel4");
                    })
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(labelUpdateDto2.getFileId()).get();
                        assertThat(file.getLabels().size()).isEqualTo(2);
                        assertThat(file.getLabels().get(0).getName()).isEqualTo("testLabel1");
                        assertThat(file.getLabels().get(1).getName()).isEqualTo("testLabel4");
                    })
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("파일이 없을 때")
        @Test
        void noFile() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url),
                            memberToken.getAccessToken(), objectMapper, labelUpdateDto1))
                    .andExpect(status().isForbidden())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url),
                            "testWrongToken", objectMapper, labelUpdateDto))
                    .andExpect(status().isForbidden())
                    .andDo(document(DOCUMENT_NAME));
        }
    }
}