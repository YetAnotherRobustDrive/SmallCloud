package org.mint.smallcloud.label.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.credentials.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.*;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.codec.DataBufferEncoder;
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
import javax.swing.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    private Member member;
    private Member admin;
    private JwtTokenDto memberToken;
    private JwtTokenDto adminToken;
    private Folder rootFolder;
    private DataNode folder;
    private DataNode dataNode;
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
        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);
        adminToken = jwtTokenProvider.generateTokenDto(adminDto);

        fileType = FileType.of("testFileType", "testType");
        fileLocation = FileLocation.of("testLocation");

        rootFolder = Folder.createRoot(member);

//        folder = DataNode.createFolder(rootFolder,"testName", member);

        dataNode = DataNode.createFile(rootFolder, fileType, fileLocation, 100L, member);
        rootFolder.addChild(dataNode);
    }

    @Nested
    @DisplayName("/label/register 라벨 생성 테스트")
    class register {
        private final String url = URL_PREFIX + "/register";
        private LabelDto labelDto;
        @BeforeEach
        void boot() {
            labelDto = LabelDto.builder()
                    .name("testLabel")
                    .owner(member)
                    .file(dataNode)
                    .build();
        }
        @DisplayName("정상적인 라벨 생성")
        @Test
        void ok() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                    fieldWithPath("name").description("라벨의 이름을 담고 있습니다."),
                    fieldWithPath("owner").description("라벨의 작성자를 담고 있습니다."),
                    fieldWithPath("file").description("라벨이 등록된 파일을 담고 있습니다."));

            mockMvc.perform(TestSnippet.securePost(url,memberToken.getAccessToken(), objectMapper, labelDto))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME, payload));
        }
    }
}