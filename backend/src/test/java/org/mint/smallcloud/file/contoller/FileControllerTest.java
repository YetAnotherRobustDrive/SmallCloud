package org.mint.smallcloud.file.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.*;
import org.mint.smallcloud.file.dto.DirectoryMoveDto;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.dto.UsageDto;
import org.mint.smallcloud.file.repository.FileRepository;
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
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        private Label favoriteLabel;
        private LabelUpdateDto labelUpdateDto1;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);
            em.flush();

            label = Label.of("testLabel1", member);
            em.persist(label);

            label1 = Label.of("testLabel2", member);
            em.persist(label1);

            favoriteLabel = Label.of(DefaultLabelType.defaultFavorite.getLabelName(), member);
            labelRepository.save(favoriteLabel);
            em.persist(favoriteLabel);


            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            dataNode1.addLabel(label1);
            dataNode1.addLabel(favoriteLabel);
            em.persist(dataNode);
            em.persist(dataNode1);
            em.flush();

            labelUpdateDto = LabelUpdateDto.builder()
                    .fileId(dataNode.getId())
                    .labels(labels)
                    .build();

            labelUpdateDto1 = LabelUpdateDto.builder()
                    .fileId(999999999999999L)
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
                        assertThat(labels.size()).isEqualTo(4);
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
                        File file = fileRepository.findById(labelUpdateDto2.getFileId()).get();
                        assertThat(file.getLabels().size()).isEqualTo(3);
                        System.out.println("즐겨찾기");
                        System.out.println(favoriteLabel);
                        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), member);
                        System.out.println(label.getName());
                        System.out.println(file.getLabels());
                        System.out.println(file.getLabels().get(0).getName());
                        System.out.println(file.getLabels().get(1).getName());
                    })
                    .andExpect((rst) -> {
                        if(labelRepository.existsByNameAndOwner(favoriteLabel.getName(), member))
                            System.out.println("favoriteLabel is exist");
                    })
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("파일이 없을 때")
        @Test
        void noFile() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url),
                            memberToken.getAccessToken(), objectMapper, labelUpdateDto1))
                    .andDo(print())
                    .andExpect(status().isNotFound())
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

    @Nested
    @DisplayName("/files/{fileId}/delete docs")
    class delete {
        String url = URL_PREFIX + "/{fileId}/delete";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private Label label;
        private Label trashLabel;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);


            label = Label.of("testLabel1", member);
            em.persist(label);


            trashLabel = Label.of(DefaultLabelType.defaultTrash.getLabelName(), member);
            em.persist(trashLabel);


            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode2.addLabel(trashLabel);
            em.persist(dataNode2);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청(라벨이 하나도 없을 때)")
        void okNoLabel() throws Exception {
            PathParametersSnippet payload = pathParameters(
                    parameterWithName("fileId").description("삭제할 파일의 id를 담고 있습니다."));

            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(trashLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME, payload));
        }
        @Test
        @DisplayName("정상 요청(라벨이 붙어있을 때)")
        void okLabel() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode1.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode1.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(2);
                        assertThat(file.getLabels().contains(trashLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("이미 라벨이 붙어 있을 때")
        void existLabel() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode2.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isForbidden())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode2.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(trashLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("파일이 존재하지 않을 때")
        void noFile() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, 100), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/files/{fileId}/restore docs")
    class restore {
        String url = URL_PREFIX + "/{fileId}/restore";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private DataNode dataNode3;
        private Label label;
        private Label trashLabel;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);


            label = Label.of("testLabel1", member);
            em.persist(label);


            trashLabel = Label.of(DefaultLabelType.defaultTrash.getLabelName(), member);
            em.persist(trashLabel);


            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode.addLabel(trashLabel);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            dataNode1.addLabel(trashLabel);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            em.persist(dataNode2);
            dataNode3 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode3.addLabel(label);
            em.persist(dataNode3);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청(휴지통 라벨만 있을 때)")
        void ok() throws Exception {
            PathParametersSnippet payload = pathParameters(
                    parameterWithName("fileId").description("복구할 파일의 id를 담고 있습니다."));

            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(0);
                        assertThat(file.getLabels().contains(trashLabel)).isFalse();
                    })
                    .andDo(document(DOCUMENT_NAME, payload));
        }
        @Test
        @DisplayName("정상 요청(휴지통 라벨과 다른 라벨이 함께있을 때)")
        void okOther() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode1.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode1.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(trashLabel)).isFalse();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("어떤 라벨도 붙어있지 않을 때")
        void noLabels() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode2.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("휴지통 라벨이 붙어있지 않을 때")
        void noTrash() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode3.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("파일이 존재하지 않을 때")
        void noFile() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, 100), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/files/{fileId}/move docs")
    class move {
        private final String url = URL_PREFIX + "/{fileId}/move";
        private Member user1;
        private Folder root;
        private Folder parent;
        private File target;
        private JwtTokenDto token1;
        private FileType fileType;
        private FileLocation fileLocation;

        @BeforeEach
        void boot() {
            Member user1 = Member.of("user1", "test", "nick");
            em.persist(user1);
            token1 = jwtTokenProvider.generateTokenDto(UserDetailsDto
                    .builder()
                    .username(user1.getUsername())
                    .password(user1.getPassword())
                    .roles(user1.getRole()).build());
            root = Folder.createRoot(user1);
            em.persist(root);
            em.flush();
            parent = (Folder) Folder.createFolder(root, "folder1", user1);
            em.persist(parent);
            em.flush();

            fileType = FileType.of("testName", "testType");
            fileLocation = FileLocation.of("testLocation");

            target = (File) File.createFile(root, fileType, fileLocation, 100L, user1);
            em.persist(target);
            em.flush();
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            DirectoryMoveDto dto1 = DirectoryMoveDto.builder()
                    .directoryId(parent.getId())
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url, target.getId()),
                                    token1.getAccessToken(), objectMapper, dto1))
                    .andExpect(status().isOk())
                    .andExpect((rst) ->
                            fileRepository
                                    .findByFileType_NameAndParentFolder_Id(target.getName(), parent.getId())
                                    .orElseThrow(Exception::new)
                    )
                    .andDo(document(DOCUMENT_NAME,
                            requestFields(
                                    fieldWithPath("directoryId").description("이동할 목적지 디렉터리 id")
                            ),
                            pathParameters(
                                    parameterWithName("fileId").description("이동시킬 파일 id")
                            )));
        }

        @DisplayName("옮길 파일이 없는 경우")
        @Test
        void fileNotFound() throws Exception {
            DirectoryMoveDto dto1 = DirectoryMoveDto.builder()
                    .directoryId(parent.getId())
                    .build();
            mockMvc.perform(
                            TestSnippet.secured(post(url, target.getId() + 100),
                                    token1.getAccessToken(), objectMapper, dto1))
                    .andExpect(status().isNotFound())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("목적지 디렉토리가 없는 경우")
        @Test
        void destNotFound() throws Exception {
            DirectoryMoveDto dto1 = DirectoryMoveDto.builder()
                    .directoryId(parent.getId() + 100)
                    .build();
            mockMvc.perform(
                            TestSnippet.secured(post(url, target.getId()),
                                    token1.getAccessToken(), objectMapper, dto1))
                    .andExpect(status().isNotFound())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("접근 권한이 없는 경우")
        @Test
        void noAccessRight() throws Exception {
            Member member2 = Member.of("user2", "test", "nick1");
            em.persist(member2);
            em.flush();
            JwtTokenDto token = jwtTokenProvider.generateTokenDto(UserDetailsDto
                    .builder()
                    .username(member2.getUsername())
                    .password(member2.getPassword())
                    .roles(member2.getRole()).build());
            DirectoryMoveDto dto1 = DirectoryMoveDto.builder()
                    .directoryId(parent.getId())
                    .build();
            mockMvc.perform(
                            TestSnippet.secured(post(url, target.getId()),
                                    token.getAccessToken(), objectMapper, dto1))
                    .andExpect(status().isForbidden())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/files/{fileId}/purge docs")
    class purge {
        private final String url = URL_PREFIX + "/{fileId}/purge";
        private Folder parent;
        private Label label;
        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);

            label = Label.of("testLabel1", member);
            em.persist(label);

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            em.persist(dataNode);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청")
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, dataNode.getId()), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME, pathParameters(
                            parameterWithName("fileId").description("완전 삭제할 파일 id")
                    )));
        }
    }

    @Nested
    @DisplayName("/files/{fileId}/favorite docs")
    class favorite {
        String url = URL_PREFIX + "/{fileId}/favorite";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private Label label;
        private Label favoriteLabel;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);

            label = Label.of("testLabel1", member);
            em.persist(label);

            favoriteLabel = Label.of(DefaultLabelType.defaultFavorite.getLabelName(), member);
            em.persist(favoriteLabel);

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode2.addLabel(favoriteLabel);
            em.persist(dataNode2);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청(라벨이 하나도 없을 때)")
        void okNoLabel() throws Exception {
            PathParametersSnippet payload = pathParameters(
                    parameterWithName("fileId").description("즐겨찾기 추가할 파일의 id를 담고 있습니다."));

            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(favoriteLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME, payload));
        }
        @Test
        @DisplayName("정상 요청(라벨이 붙어있을 때)")
        void okLabel() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode1.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode1.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(2);
                        assertThat(file.getLabels().contains(favoriteLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("이미 라벨이 붙어 있을 때")
        void existLabel() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode2.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isForbidden())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode2.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(favoriteLabel)).isTrue();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("파일이 존재하지 않을 때")
        void noFile() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, 100), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/files/{fileId}/unfavorite docs")
    class unFavorite {
        String url = URL_PREFIX + "/{fileId}/unfavorite";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private DataNode dataNode3;
        private Label label;
        private Label favoriteLabel;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);

            label = Label.of("testLabel1", member);
            em.persist(label);

            favoriteLabel = Label.of(DefaultLabelType.defaultFavorite.getLabelName(), member);
            em.persist(favoriteLabel);

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode.addLabel(favoriteLabel);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            dataNode1.addLabel(favoriteLabel);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            em.persist(dataNode2);
            dataNode3 = DataNode.createFile(parent, fileType, fileLocation, 100L, member);
            dataNode3.addLabel(label);
            em.persist(dataNode3);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청(즐겨찾기 라벨만 있을 때)")
        void ok() throws Exception {
            PathParametersSnippet payload = pathParameters(
                    parameterWithName("fileId").description("즐겨찾기 제거할 파일의 id를 담고 있습니다."));

            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(0);
                        assertThat(file.getLabels().contains(favoriteLabel)).isFalse();
                    })
                    .andDo(document(DOCUMENT_NAME, payload));
        }
        @Test
        @DisplayName("정상 요청(즐겨찾기 라벨과 다른 라벨이 함께있을 때)")
        void okOther() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode1.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect((rst) -> {
                        File file = fileRepository.findById(dataNode1.getId()).orElseThrow();
                        assertThat(file.getLabels().size()).isEqualTo(1);
                        assertThat(file.getLabels().contains(favoriteLabel)).isFalse();
                    })
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("어떤 라벨도 붙어있지 않을 때")
        void noLabels() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode2.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("즐겨찾기 라벨이 붙어있지 않을 때")
        void noFavorite() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode3.getId()), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("파일이 존재하지 않을 때")
        void noFile() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, 100), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(post(url, dataNode.getId()), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/files/search docs")
    class search {
        String url = URL_PREFIX + "/search?q={q}";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private DataNode dataNode3;
        private Label label;
        private Label favoriteLabel;
        private FileType fileType1;
        private FileType fileType2;
        private FileType fileType3;


        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);

            label = Label.of("testLabel1", member);
            em.persist(label);

            fileType1 = FileType.of("testType1", "testType1");
            fileType2 = FileType.of("Type2", "testType2");
            fileType3 = FileType.of("test", "testType3");

            favoriteLabel = Label.of(DefaultLabelType.defaultFavorite.getLabelName(), member);
            em.persist(favoriteLabel);

            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            dataNode.addLabel(favoriteLabel);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType1, fileLocation, 100L, member);
            dataNode1.addLabel(label);
            dataNode1.addLabel(favoriteLabel);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType2, fileLocation, 100L, member);
            em.persist(dataNode2);
            dataNode3 = DataNode.createFile(parent, fileType3, fileLocation, 100L, member);
            dataNode3.addLabel(label);
            em.persist(dataNode3);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청(파일 이름으로 검색)")
        void ok() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(get(url, "test"), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME, requestParameters(
                            parameterWithName("q").description("검색할 파일 이름을 담고 있습니다.")
                    )));
        }
    }

    @Nested
    @DisplayName("/usage docs")
    class usage {
        String url = URL_PREFIX + "/usage";
        private Folder parent;
        private DataNode dataNode;
        private DataNode dataNode1;
        private DataNode dataNode2;
        private DataNode dataNode3;

        @BeforeEach
        public void boot() {
            em.persist(rootFolder);
            parent = (Folder) Folder.createFolder(rootFolder, "folder1", member);
            em.persist(parent);


            dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
            em.persist(dataNode);
            dataNode1 = DataNode.createFile(parent, fileType, fileLocation, 200L, member);
            em.persist(dataNode1);
            dataNode2 = DataNode.createFile(parent, fileType, fileLocation, 300L, member);
            em.persist(dataNode2);
            dataNode3 = DataNode.createFile(parent, fileType, fileLocation, 120L, member);
            em.persist(dataNode3);
            em.flush();
        }

        @Test
        @DisplayName("정상 요청")
        void ok() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(get(url), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        UsageDto response = objectMapper.readValue(result.getResponse().getContentAsString(), UsageDto.class);
                        assertThat(response.getUsed()).isEqualTo(820L); //setup 부분에 하나 더 있음.
                    })
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(get(url), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME));
        }
    }
}