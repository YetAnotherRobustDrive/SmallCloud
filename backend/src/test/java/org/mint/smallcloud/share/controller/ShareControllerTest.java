package org.mint.smallcloud.share.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.dto.ShareType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class ShareControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/share";
    private final String DOCUMENT_NAME = "Share/{ClassName}/{methodName}";

    @Autowired
    private EntityManager em;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;

    private Member user1;
    private Folder rootFolder1;
    private Member user2;
    private Folder rootFolder2;
    private String token1;
    private String token2;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
        user1 = Member.of("user1", "user1", "user1");
        user2 = Member.of("user2", "user2", "user2");
        em.persist(user1);
        em.persist(user2);
        em.flush();
        rootFolder1 = Folder.createRoot(user1);
        rootFolder2 = Folder.createRoot(user2);
        em.persist(rootFolder1);
        em.persist(rootFolder2);
        em.flush();
        token1 = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(user1.getUsername())
                .password(user1.getPassword())
                .roles(user1.getRole())
                .disabled(false)
                .build()
        ).getAccessToken();
        token2 = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(user2.getUsername())
                .password(user2.getPassword())
                .roles(user2.getRole())
                .disabled(false)
                .build()
        ).getAccessToken();

    }

    @Nested
    @DisplayName("create")
    class Create {
        private final String url = URL_PREFIX + "/create";

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, token1, objectMapper,
                ShareRequestDto.builder().fileId(rootFolder1.getId()).targetName(user2.getUsername()).type(ShareType.MEMBER).build()))
                .andExpect((rst) -> assertThat(rootFolder1.canAccessUser(user2)).isTrue())
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("fileId").description("공유할 파일의 아이디"),
                        fieldWithPath("targetName").description("공유할 대상의 이름"),
                        fieldWithPath("type").description("공유할 대상의 타입")
                    )));
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {
        Share share1;
        String url = URL_PREFIX + "/delete";

        @BeforeEach
        void boot() {
            share1 = Share.of(user1, rootFolder1);
            em.persist(share1);
            em.flush();
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, token1, objectMapper, ShareRequestDto.builder().fileId(rootFolder1.getId()).type(ShareType.MEMBER).targetName(user1.getUsername()).build()))
                .andExpect((rst) -> assertThat(rootFolder1.canAccessUser(user2)).isFalse())
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("fileId").description("공유할 파일의 아이디"),
                        fieldWithPath("targetName").description("공유할 대상의 이름"),
                        fieldWithPath("type").description("공유할 대상의 타입")
                    )));
        }
    }

}