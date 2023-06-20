package org.mint.smallcloud.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.admin.dto.ChangePasswordDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mint.smallcloud.TestSnippet.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AdminControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/admin";
    private final String DOCUMENT_NAME = "Admin/{ClassName}/{methodName}";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    private EntityManager em;

    private Member admin;
    private Member user1;
    private JwtTokenDto adminToken;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();

        admin = Member.createAdmin("admin", "admin", "admin");
        user1 = Member.createCommon("user1", "user1", "user1");
        em.persist(admin);
        em.persist(user1);
        em.flush();
        adminToken = jwtTokenProvider.generateTokenDto(UserDetailsDto.builder()
            .username(admin.getUsername())
            .password(admin.getPassword())
            .roles(admin.getRole())
            .disabled(false).build());
    }

    @Nested
    @DisplayName("/lock docs")
    class Lock {
        final String url = URL_PREFIX + "/lock/{username}";
        @DisplayName("정상요청")
        @Test
        void ok () throws Exception {
            user1.unlock();
            assertThat(user1.isLocked()).isFalse();
            mockMvc.perform(TestSnippet
                    .secured(post(url, user1.getUsername()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    assertThat(em.createQuery("select m from Member m where m.username = :username", Member.class)
                        .setParameter("username", user1.getUsername())
                        .getSingleResult()
                        .isLocked()).isTrue();
                })
                .andDo(document(DOCUMENT_NAME, pathParameters(
                    parameterWithName("username").description("사용자 아이디")
                )));
        }
    }

    @Nested
    @DisplayName("/unlock docs")
    class Unlock {
        final String url = URL_PREFIX + "/unlock/{username}";
        @DisplayName("정상요청")
        @Test
        void ok () throws Exception {
            user1.lock();
            assertThat(user1.isLocked()).isTrue();
            mockMvc.perform(TestSnippet
                    .secured(post(url, user1.getUsername()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    assertThat(em.createQuery("select m from Member m where m.username = :username", Member.class)
                        .setParameter("username", user1.getUsername())
                        .getSingleResult()
                        .isLocked()).isFalse();
                })
                .andDo(document(DOCUMENT_NAME, pathParameters(
                    parameterWithName("username").description("사용자 아이디")
                )));
        }
    }

    @Nested
    @DisplayName("/change-password docs")
    class ChangePassword {
        final String url = URL_PREFIX + "/change-password/{username}";
        final String newPassword = "qweqwewq";
        @DisplayName("정상요청")
        @Test
        void ok () throws Exception {
            ChangePasswordDto dto = ChangePasswordDto.builder()
                .password(newPassword)
                .build();

            mockMvc.perform(TestSnippet
                    .secured(post(url, objectMapper, dto, user1.getUsername()), adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    assertThat(em.createQuery("select m from Member m where m.username = :username", Member.class)
                        .setParameter("username", user1.getUsername())
                        .getSingleResult().verifyPassword(newPassword)).isTrue();
                })
                .andDo(document(DOCUMENT_NAME, pathParameters(
                    parameterWithName("username").description("사용자 아이디")
                ),
                requestFields(
                    fieldWithPath("password").description("새로운 비밀번호")
                )));
        }
    }
}