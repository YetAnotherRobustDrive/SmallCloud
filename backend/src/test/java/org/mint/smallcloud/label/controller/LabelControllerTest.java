package org.mint.smallcloud.label.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.*;
import org.mint.smallcloud.file.dto.DataNodeLabelDto;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserLabelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
    private final String DOCUMENT_NAME = "Label/{ClassName}/{methodName}";
    private UserDetailsDto userDetailsDto;
    private UserDetailsDto adminDto;
    private UserLabelDto userLabelDto;
    private Member member;
    private Member admin;
    private JwtTokenDto memberToken;
    private JwtTokenDto adminToken;
    private Folder rootFolder;
    private DataNode folder;
    private DataNode dataNode;
    private DataNodeLabelDto dataNodeLabelDto;
    private FileType fileType;
    private FileLocation fileLocation;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        member = Member.createCommon("testMemberName", "testPw", "testNickname");
        admin = Member.createAdmin("testAdminName", "testPw", "testNickname");
        em.persist(member);
        em.persist(admin);
        em.flush();

        userDetailsDto = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();
        adminDto = UserDetailsDto.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.isLocked())
                .roles(admin.getRole())
                .build();
        userLabelDto = UserLabelDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();

        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);
        adminToken = jwtTokenProvider.generateTokenDto(adminDto);

        fileType = FileType.of("testFileType", "testType");
        fileLocation = FileLocation.of("testLocation");

        rootFolder = Folder.createRoot(member);
        dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        em.persist(rootFolder);
        em.persist(dataNode);
        em.flush();
        rootFolder.addChild(dataNode);

        dataNodeLabelDto = DataNodeLabelDto.builder()
                .id(dataNode.getId())
                .createdDate(dataNode.getCreatedDate())
                .name(dataNode.getName())
                .parentFolderId(dataNode.getParentFolder().getId())
                .build();
    }

    @Nested
    @DisplayName("/label/register 라벨 생성 테스트")
    class register {
        private final String url = URL_PREFIX + "/register";
        private LabelDto labelDto;
        private LabelDto noLabelNameDto;
        private LabelDto noOwnerDto;
        private LabelDto noFileDto;
        @BeforeEach
        void boot() {
            labelDto = LabelDto.builder()
                    .name("testLabel")
                    .owner(userLabelDto)
                    .file(dataNodeLabelDto)
                    .build();

            noLabelNameDto = LabelDto.builder()
                    .owner(userLabelDto)
                    .file(dataNodeLabelDto)
                    .build();
            noOwnerDto = LabelDto.builder()
                    .name("testLabel")
                    .owner(null)
                    .file(dataNodeLabelDto)
                    .build();

            noFileDto = LabelDto.builder()
                    .name("testLabel")
                    .owner(userLabelDto)
                    .file(null)
                    .build();
        }
        @DisplayName("정상적인 라벨 생성")
        @Test
        void ok() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                    fieldWithPath("name").description("라벨의 이름을 담고 있습니다."),
                    fieldWithPath("owner.username").description("라벨 작성자의 이름를 담고 있습니다."),
                    fieldWithPath("owner.nickname").description("라벨 작성자의 닉네임을 담고 있습니다."),
                    fieldWithPath("file.id").description("라벨이 등록된 파일의 id를 담고 있습니다."),
                    fieldWithPath("file.name").description("라벨이 등록된 파일의 이름을 담고 있습니다."),
                    fieldWithPath("file.parentFolderId").description("라벨이 등록된 파일의 부모 폴더 id를 담고 있습니다."),
                    fieldWithPath("file.createdDate").description("라벨이 등록된 파일의 생성일자를 담고 있습니다."));

            mockMvc.perform(TestSnippet.securePost(url,memberToken.getAccessToken(), objectMapper, labelDto))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("라벨 이름이 존재하지 않음")
        @Test
        void noLabelName() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, noLabelNameDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("라벨 소유자가 존재하지 않음")
        @Test
        void noOwner() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, noOwnerDto))
                    .andExpect(status().isForbidden())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("라벨의 파일이 존재하지 않음")
        @Test
        void noFile() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, noFileDto))
                    .andExpect(status().isForbidden())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, labelDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/labels/deregister 라벨 영구 제거 테스트")
    class deregister {
        private final String url = URL_PREFIX + "/deregister";
        private LabelDto labelDto;
        @BeforeEach
        void boot() {
            labelDto = LabelDto.builder()
                    .name("testLabel")
                    .owner(userLabelDto)
                    .file(dataNodeLabelDto)
                    .build();
            Label label = Label.of(
                    labelDto.getName(),
                    member);
            label.addFile(dataNode);
            labelRepository.save(label);
        }
        @DisplayName("정상적인 라벨 영구 제거")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, labelDto))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, labelDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }
}