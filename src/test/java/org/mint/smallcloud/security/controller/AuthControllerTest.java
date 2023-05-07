package org.mint.smallcloud.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.security.jwt.JwtTokenProvider;
import org.mint.smallcloud.user.Roles;
import org.mint.smallcloud.user.User;
import org.mint.smallcloud.user.repository.UserRepository;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/auth";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("/auth/register document")
    public void register() throws Exception {
        final String url = URL_PREFIX + "/register";
        RequestFieldsSnippet payload = requestFields(
                fieldWithPath("id").description("user id"),
                fieldWithPath("name").description("user nickname"),
                fieldWithPath("password").description("user password"));
        RegisterDto registerDto =
            RegisterDto.builder()
                    .id("test1")
                    .name("testName")
                    .password("pw").build();

        this.mockMvc.perform(TestSnippet.post(url, objectMapper, registerDto))
                .andExpect(status().isOk())
                .andDo(document("Register", payload));

        this.mockMvc.perform(TestSnippet.post(url, objectMapper, registerDto))
                .andExpect(status().isForbidden())
                .andDo(document("RegisterFail", payload));
    }

    @Test
    @DisplayName("/auth/login document")
    public void login() throws Exception {
        final String url = URL_PREFIX + "/login";
        RequestFieldsSnippet payload = requestFields(
                fieldWithPath("id").description("user id"),
                fieldWithPath("password").description("user password"));
        LoginDto loginDto1 = LoginDto.builder()
                .id("user1")
                .password("pw")
                .build();
        LoginDto loginDto2 = LoginDto.builder()
                .id("user2")
                .password("pw")
                .build();
        LoginDto wrongPasswordLoginDto1 = LoginDto.builder()
                .id("user1")
                .password("pw1")
                .build();
        userRepository.save(User.of(loginDto1.getId(),
            loginDto1.getPassword(), "nickname"));

        this.mockMvc.perform(TestSnippet.post(url, objectMapper, loginDto1))
                .andExpect(status().isOk())
                .andDo(document("Login", payload));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, loginDto2))
                .andExpect(status().isForbidden())
                .andDo(document("NotFoundUserLogin", payload));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, wrongPasswordLoginDto1))
                .andExpect(status().isForbidden())
                .andDo(document("WrongPassword", payload));
    }

    @Test
    @DisplayName("/auth/refresh document")
    public void refresh() throws Exception {
        final String url = URL_PREFIX + "/refresh";
        LoginDto loginDto1 = LoginDto.builder()
                .id("user1")
                .password("pw")
                .build();
        String refresh = "refresh";
        String success = "success";

        /* token provider mocking */
        when(tokenProvider.generateAccessToken(eq(refresh), any()))
                .thenReturn("success");
        doNothing().when(tokenProvider).validateToken(refresh);
        when(tokenProvider.resolveTokenFromHeader(any()))
                .thenCallRealMethod();
        /* token provider mocking */

        userRepository.save(User.of(loginDto1.getId(),
                loginDto1.getPassword(), "nickname"));
        this.mockMvc.perform(TestSnippet.secureGet(url, refresh))
                .andExpect(status().isOk())
                .andExpect(content().string(success))
                .andDo(document("Refresh"));

        doCallRealMethod().when(tokenProvider).validateToken(any());
        this.mockMvc.perform(TestSnippet.secureGet(url, "abc"))
                .andExpect(status().isBadRequest())
                .andDo(document("RefreshBadToken"));
    }
}