package org.mint.smallcloud.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/auth";
    private final String DOCUMENT_NAME = "Auth/{ClassName}/{methodName}";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private LoginDto user1;
    private LoginDto user2;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();

        user1 = LoginDto.builder()
            .id("user1")
            .password("pw1")
            .build();

        user2 = LoginDto.builder()
            .id("user2")
            .password("pw2")
            .build();
    }

    @Nested
    @DisplayName("/auth/register document")
    class Register {
        private final String url = URL_PREFIX + "/register";
        private final RegisterDto registerDto =
            RegisterDto.builder()
                .id("test1")
                .name("안녕")
                .password("pw").build();

        @Test
        @DisplayName("정상 요청")
        public void ok() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                fieldWithPath("id").description("user id"),
                fieldWithPath("name").description("user nickname"),
                fieldWithPath("password").description("user password"));

            mockMvc.perform(TestSnippet.post(url, objectMapper, registerDto))
                .andExpect(status().isOk())
                .andExpect((result) -> assertNotNull(memberRepository.findByUsername(registerDto.getId()).orElse(null)))
                .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("이미 존재하는 유저")
        @Test
        void duplicated() throws Exception {
            em.persist(Member.of(registerDto.getId(), registerDto.getPassword(), registerDto.getName()));
            mockMvc.perform(TestSnippet.post(url, objectMapper, registerDto))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 포멧으로 된 dto")
        @Test
        void wrongFormatDto() throws Exception {
            RegisterDto wrongFormat =
                RegisterDto.builder()
                    .id("ifjii9fdsafdsafdsafdsf0j9")
                    .name("안녕")
                    .password("fdsafdsafdsafdsafdsafdas")
                    .build();

            mockMvc.perform(TestSnippet.post(url, objectMapper, wrongFormat))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/auth/login document")
    class Login {
        private final String url = URL_PREFIX + "/login";
        private LoginDto wrongPasswordLoginDto1;

        @BeforeEach
        void boot() {
            wrongPasswordLoginDto1 = LoginDto.builder()
                .id(user1.getId())
                .password(user1.getPassword() + "abc")
                .build();
            em.persist(Member.of(user1.getId(), user1.getPassword(), "nickname"));
            em.flush();
        }

        @Test
        @DisplayName("/auth/login document")
        public void ok() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                fieldWithPath("id").description("user id"),
                fieldWithPath("password").description("user password"));

            mockMvc.perform(TestSnippet.post(url, objectMapper, user1))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("등록되지 않은 유저 로그인")
        @Test
        void notRegistered() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, user2))
                .andExpect(status().isNotFound())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("패스워드가 틀림")
        @Test
        void wrongPassword() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, wrongPasswordLoginDto1))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 포멧으로 된 dto")
        @Test
        void wrongFormat() throws Exception {
            LoginDto notValidDto = LoginDto.builder()
                .id("fjiowejfioewjoifjweoijf@@@ioewjofijewiofjewiojioewj")
                .password("fjioewjifojewiofjewiojfioewjfioewjiofjwe")
                .build();

            mockMvc.perform(TestSnippet.post(url, objectMapper, notValidDto))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

    }

    @Nested
    @DisplayName("/auth/elevate document")
    class Elevate {
        final String url = URL_PREFIX + "/elevate";
        Map<String, String> map;

        private JwtTokenDto token;

        @BeforeEach
        void boot() {
            map = new HashMap<>();
            em.persist(Member.of(user1.getId(), user1.getPassword(), "nickname"));
            em.flush();
            token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.COMMON)
                    .disabled(false)
                    .build());
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                fieldWithPath("password").description("user password"));
            map.put("password", user1.getPassword());
            mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken(), objectMapper, map))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("패스워드가 틀림")
        @Test
        void wrongPassword() throws Exception {
            map.put("password", user1.getPassword() + "abc");
            mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken(), objectMapper, map))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("로그인 토큰이 잘못됨")
        @Test
        void wrongToken() throws Exception {
            map.put("password", user1.getPassword());
            mockMvc.perform(TestSnippet.post(url, objectMapper, map))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("패스워드 포멧이 잘못됨")
        @Test
        void wrongFormatPassword() throws Exception {
            map.put("password", "123jfkldsajfkldjsaklfjdsalj");
            mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken(), objectMapper, map))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/auth/deregister document")
    class Deregister {
        private final String url = URL_PREFIX + "/deregister";

        private JwtTokenDto token;
        private Member member1;

        @BeforeEach
        void boot() {
            member1 = Member.of(user1.getId(),
                user1.getPassword(), "nickname");
            em.persist(member1);
            memberRepository.save(Member.of(user2.getId(),
                user2.getPassword(), "nickname"));
            em.flush();
        }

        @Test
        @DisplayName("정상 요청")
        void  ok() throws Exception {
            JwtTokenDto privilegeToken = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.PRIVILEGE)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.securePost(url, privilegeToken.getAccessToken()))
                .andExpect(status().isOk())
                    .andExpect((rst) -> {
                        assertTrue(member1.isLocked());
                    }).andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("권한이 없는 유저가 요청")
        void notPrivilege() throws Exception {
            token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user2.getId())
                    .password(user2.getPassword())
                    .roles(Role.COMMON)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken()))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

    }

    @Nested
    @DisplayName("/auth/refresh document")
    class Refresh {
        final String url = URL_PREFIX + "/refresh";
        private JwtTokenDto token;

        @BeforeEach
        void boot() {
            em.persist(Member.of(user1.getId(),
                user1.getPassword(), "nickname"));
            em.flush();
            token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.COMMON)
                    .disabled(false)
                    .build());
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, token.getRefreshToken()))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME,
                    responseFields(
                        fieldWithPath("result")
                            .type(JsonFieldType.STRING)
                            .description("refresh token")
                    )));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, "abc"))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/auth/privileged document")
    class Privileged {
        private final String url = URL_PREFIX + "/privileged";

        @BeforeEach
        void boot() {
            em.persist(Member.of(user1.getId(),
                user1.getPassword(), "nickname"));
            em.flush();
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            JwtTokenDto token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.PRIVILEGE)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().json("{result: true}"))
                .andDo(document(DOCUMENT_NAME,
                    responseFields(
                        fieldWithPath("result")
                            .type(JsonFieldType.BOOLEAN)
                            .description("privileged된 유저라면 true")
                    )));
        }

        @DisplayName("권한이 없는 유저가 요청")
        @Test
        void notPrivilege() throws Exception {
            JwtTokenDto token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.COMMON)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().json("{result: false}"))
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/auth/admin-check document")
    class AdminCheck {
        private final String url = URL_PREFIX + "/admin-check";
        private Member admin;

        @BeforeEach
        void boot() {
            admin = Member.createAdmin("admin1", "password", "nickname");
            em.persist(admin);
            em.persist(Member.of(user1.getId(),
                user1.getPassword(), "nickname"));
            em.flush();
        }

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            JwtTokenDto token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(admin.getUsername())
                    .password(admin.getPassword())
                    .roles(Role.ADMIN)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().json("{result: true}"))
                .andDo(document(DOCUMENT_NAME,
                    responseFields(
                        fieldWithPath("result")
                            .type(JsonFieldType.BOOLEAN)
                            .description("admin인 유저라면 true")
                    )));
        }

        @DisplayName("권한이 없는 유저가 요청")
        @Test
        void notPrivilege() throws Exception {
            JwtTokenDto token = jwtTokenProvider.generateTokenDto(
                UserDetailsDto.builder()
                    .username(user1.getId())
                    .password(user1.getPassword())
                    .roles(Role.COMMON)
                    .disabled(false)
                    .build());
            mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().json("{result: false}"))
                .andDo(document(DOCUMENT_NAME));
        }
    }
}