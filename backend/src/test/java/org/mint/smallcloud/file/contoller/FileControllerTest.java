package org.mint.smallcloud.file.contoller;

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
import org.mint.smallcloud.label.domain.Label;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
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

    }

    @Nested
    @DisplayName("/files/{fileId}/update/label docs")
    class labelUpdate {
        private final String url =  URL_PREFIX + "/{fileId}/update/label";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode noDataNode;
        private Label label;
        private MultiValueMap<String, String> info;
        private MultiValueMap<String, String> info1;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);
            em.flush();

            label = Label.of("testName", member);
            em.persist(label);
            em.flush();

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            noDataNode = null;
            em.persist(dataNode);
            em.persist(dataNode1);
            em.flush();

            info = new LinkedMultiValueMap<>();
            info.add("labels", "testLabel1");
            info.add("labels", "testLabel2");
            info.add("labels", "testLabel3");
            info.add("fileId", dataNode.getId().toString());

            info1 = new LinkedMultiValueMap<>();
            info1.add("labels", "testLabel1");
            info1.add("labels", "testLabel2");
            info1.add("labels", "testLabel3");
            info1.add("fileId", "100");
        }
        @DisplayName("정상 요청(파일에 라벨이 하나도 없을 때)")
        @Test
        void okNoLabels() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, dataNode.getId()).params(info), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME,
                            requestParameters(
                                    parameterWithName("labels").description("업데이트할 라벨 리스트"),
                                    parameterWithName("fileId").description("업데이트할 파일 id")
                            )));
        }

        @DisplayName("정상 요청(파일에 라벨이 일부 존재할 때)")
        @Test
        void okSomeLabels() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, dataNode1.getId()).params(info), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("파일이 없을 때")
        @Test
        void noFile() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, dataNode.getId()).params(info1), memberToken.getAccessToken()))
                    .andExpect(status().isForbidden())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, dataNode.getId()).params(info), "testWrongToken"))
                    .andExpect(status().isForbidden())
                    .andDo(document(DOCUMENT_NAME));
        }

        /**
         * 폴더에도 라벨 붙이기
         * 파일에 라벨이 하나도 없을 때  ㅇ
         * 파일에 라벨이 일부 존재할 때  ㅇ
         *
         * 파일이 없을 때 ------
         * 토큰이 잘못 됐을 때 ㅇ
         */
    }
}