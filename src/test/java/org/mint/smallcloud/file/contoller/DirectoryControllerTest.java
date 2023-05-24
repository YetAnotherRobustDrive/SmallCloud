package org.mint.smallcloud.file.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryCreateDto;
import org.mint.smallcloud.file.repository.FolderRepository;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class DirectoryControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/directory";
    private final String DOCUMENT_NAME = "Directory/{ClassName}/{methodName}";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    EntityManager em;

    @Autowired
    FolderRepository folderRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
    }

    @Nested
    @DisplayName("/directory/{directoryId}/create docs")
    class Create {
        private final String url = URL_PREFIX + "/{directoryId}/create";
        private Member user1;
        private Folder folder1;
        private JwtTokenDto token1;

        @BeforeEach
        void boot() {
            user1 = Member.of("user1", "test", "nick");
            em.persist(user1);
            token1 = jwtTokenProvider.generateTokenDto(UserDetailsDto
                .builder()
                .username(user1.getUsername())
                .password(user1.getPassword())
                .roles(user1.getRole()).build());
            folder1 = Folder.createRoot(user1);
            em.persist(folder1);
            em.flush();

        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            DirectoryCreateDto dto1 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId()),
                        token1.getAccessToken(), objectMapper, dto1))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    Folder folder = folderRepository.findByFileType_NameAndParentFolder_Id(dto1.getName(), folder1.getId())
                        .orElseThrow(Exception::new);
                    assertEquals(dto1.getName(), folder.getName());
                })
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("name").description("생성할 디렉토리 이름")
                    ),
                    pathParameters(
                        parameterWithName("directoryId").description("디렉토리 아이디")
                    )));
        }

        @DisplayName("이름이 중복된 경우")
        @Test
        void duplicatedName() throws Exception {
            DirectoryCreateDto dto1 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId()),
                        token1.getAccessToken(), objectMapper, dto1))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    Folder folder = folderRepository.findByFileType_NameAndParentFolder_Id(dto1.getName(), folder1.getId())
                        .orElseThrow(Exception::new);
                    assertEquals(dto1.getName(), folder.getName());
                });
            DirectoryCreateDto dto2 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId()),
                        token1.getAccessToken(), objectMapper, dto2))
                .andExpect((rst) -> {
                    folderRepository.findByFileType_NameAndParentFolder_Id(dto2.getName() + "_1", folder1.getId())
                        .orElseThrow(Exception::new);
                })
                .andExpect(status().isOk());
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId()),
                        token1.getAccessToken(), objectMapper, dto2))
                .andExpect((rst) -> {
                    folderRepository.findByFileType_NameAndParentFolder_Id(dto2.getName() + "_2", folder1.getId())
                        .orElseThrow(Exception::new);
                })
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("부모 디렉토리가 없는 경우")
        @Test
        void parentNotFound() throws Exception {
            DirectoryCreateDto dto1 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId() + 1),
                        token1.getAccessToken(), objectMapper, dto1))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("토큰이 없는 경우")
        @Test
        void noToken() throws Exception {
            DirectoryCreateDto dto1 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    post(url, folder1.getId(), objectMapper, dto1))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("접근 권한이 없는 유저인 경우")
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
            DirectoryCreateDto dto1 = DirectoryCreateDto.builder()
                .name("test")
                .build();
            mockMvc.perform(
                    TestSnippet.secured(post(url, folder1.getId()),
                        token.getAccessToken(), objectMapper, dto1))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }
    }
}