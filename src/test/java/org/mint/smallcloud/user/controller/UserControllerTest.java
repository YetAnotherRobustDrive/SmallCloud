package org.mint.smallcloud.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.RegisterDto;
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
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNull;
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
class UserControllerTest {

    private MockMvc mockMvc;
    private final String URL_PREFIX = "/users";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    private EntityManager em;

    private Member admin;
    private Member member1;
    private JwtTokenDto adminToken;
    private JwtTokenDto memberToken;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
        admin = Member.createAdmin("admin", "pw1", "admin");
        em.persist(admin);
        adminToken = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole())
                .disabled(false)
                .build()
        );
        member1 = Member.of("user1", "pw1", "nickname");
        em.persist(member1);
        em.flush();
        memberToken = jwtTokenProvider.generateTokenDto(UserDetailsDto.builder()
            .username(member1.getUsername())
            .password(member1.getPassword())
            .roles(member1.getRole())
            .disabled(false)
            .build());
    }

    @Nested
    @DisplayName("/users/{username}/delete docs")
    class delete {
        final String url = URL_PREFIX + "/{username}/delete";

        @DisplayName("정상 요청")
        @Test
        public void find() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect((rst) -> assertNull(em.find(Member.class, member1.getId())))
                .andDo(document("UserDelete",
                    pathParameters(
                        parameterWithName("username").description("삭제하려는 유저의 아이디")
                    )));
        }


        @DisplayName("잘못된 형태의 유저id")
        @Test
        public void wrongFormat() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, "jfieowoifwejiofjioewjfioewjiof"), adminToken.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("UserDeleteUsernameToLong"));
        }

        @DisplayName("유저가 잘못된 접근")
        @Test
        public void unauthorized() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), memberToken.getAccessToken()))
                .andExpect(status().isForbidden())
                .andDo(document("UserDeleteUnauthorized"));
        }

        @DisplayName("없는 유저 삭제 요청")
        @Test
        public void noUser() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, "fewfas"), adminToken.getAccessToken()))
                .andExpect(status().isForbidden())
                .andDo(document("UserDeleteNotUser"));
        }
    }
    
    @Nested
    @DisplayName("/users docs")
    class register {
        private String url = URL_PREFIX;

        @DisplayName("정상 요청")
        @Test
        public void find() throws Exception {
            RegisterDto registerDto = RegisterDto.builder()
                .id("user2")
                .password("pw2")
                .name("name")
                .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, registerDto))
                .andExpect(status().isOk())
                .andDo(document("UserRegister", requestFields(
                    fieldWithPath("id").description("user id"),
                    fieldWithPath("name").description("user nickname"),
                    fieldWithPath("password").description("user password"))));
        }
    }

}