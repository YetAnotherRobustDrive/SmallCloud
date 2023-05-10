package org.mint.smallcloud.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
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
        memberRepository.save(Member.of(loginDto1.getId(),
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
    @DisplayName("/auth/elevate document")
    public void elevate() throws Exception {
        final String url = URL_PREFIX + "/elevate";
        RequestFieldsSnippet payload = requestFields(
            fieldWithPath("password").description("user password"));
        LoginDto loginDto1 = LoginDto.builder()
            .id("user1")
            .password("pw")
            .build();
        Map<String, String> map = new HashMap<>();
        map.put("password", "pw");
        memberRepository.save(Member.of(loginDto1.getId(),
            loginDto1.getPassword(), "nickname"));
        JwtTokenDto token = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username("user1")
                .password("pw")
                .roles(Role.COMMON)
                .disabled(false)
                .build());
        this.mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken(), objectMapper, map))
            .andExpect(status().isOk())
            .andDo(document("Elevate", payload));
        map.clear();
        map.put("password", "123");
        this.mockMvc.perform(TestSnippet.securePost(url, token.getAccessToken(), objectMapper, map))
            .andExpect(status().isForbidden())
            .andDo(document("ElevateWrongPassword"));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, map))
            .andExpect(status().isForbidden())
            .andDo(document("ElevateUnauth"));
    }

    @Test
    @DisplayName("/auth/deregister document")
    public void deregister() throws Exception {
        final String url = URL_PREFIX + "/deregister";
        LoginDto loginDto1 = LoginDto.builder()
            .id("user1")
            .password("pw")
            .build();
        LoginDto loginDto2 = LoginDto.builder()
            .id("user2")
            .password("pw")
            .build();

        memberRepository.save(Member.of(loginDto1.getId(),
            loginDto1.getPassword(), "nickname"));
        memberRepository.save(Member.of(loginDto2.getId(),
            loginDto2.getPassword(), "nickname"));

        JwtTokenDto token = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(loginDto1.getId())
                .password(loginDto1.getPassword())
                .roles(Role.COMMON)
                .disabled(false)
                .build());
        Map<String, String> map = new HashMap<>();
        map.put("password", "pw");
        JwtTokenDto privilegeToken = objectMapper.readValue(
            this.mockMvc.perform(TestSnippet.securePost(URL_PREFIX + "/elevate", token.getAccessToken(), objectMapper, map))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
            , JwtTokenDto.class);

        this.mockMvc.perform(TestSnippet.securePost(url, privilegeToken.getAccessToken()))
            .andExpect(status().isOk())
            .andDo(document("Deregister"));

        assertNull(memberRepository
            .findByUsername(loginDto1.getId()).orElse(null));

        JwtTokenDto commonToken = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(loginDto2.getId())
                .password(loginDto2.getPassword())
                .roles(Role.COMMON)
                .disabled(false)
                .build());
        this.mockMvc.perform(TestSnippet.securePost(url, commonToken.getAccessToken()))
            .andExpect(status().isForbidden())
            .andDo(document("DeregisterPrivilege"));
    }

    @Test
    @DisplayName("/auth/refresh document")
    public void refresh() throws Exception {
        final String url = URL_PREFIX + "/refresh";
        Member member = Member.of("test1", "password", "nickname");
        memberRepository.save(member);
        UserDetailsDto userDto = UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .disabled(member.isLocked())
            .roles(member.getRole()).build();
        JwtTokenDto refresh = jwtTokenProvider.generateTokenDto(userDto);

        this.mockMvc.perform(TestSnippet.secureGet(url, refresh.getRefreshToken()))
            .andExpect(status().isOk())
            .andDo(document("Refresh",
                responseFields(
                fieldWithPath("result")
                    .type(JsonFieldType.STRING)
                    .description("refresh token")
            ) ));

        this.mockMvc.perform(TestSnippet.secureGet(url, "abc"))
            .andExpect(status().isBadRequest())
            .andDo(document("RefreshBadToken"));
    }

    @Test
    @DisplayName("/auth/privileged document")
    public void privileged() throws Exception {
        final String url = URL_PREFIX + "/privileged";
        Member member = Member.of("test1", "password", "nickname");
        memberRepository.save(member);
        UserDetailsDto userDto = UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .disabled(member.isLocked())
            .roles(Role.PRIVILEGE).build();
        JwtTokenDto token = jwtTokenProvider.generateTokenDto(userDto);

        this.mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
            .andExpect(status().isOk())
            .andExpect(content().json("{result: true}"))
            .andDo(document("Privileged",
                responseFields(
                    fieldWithPath("result")
                        .type(JsonFieldType.BOOLEAN)
                        .description("privileged된 유저라면 true")
                )));

        userDto = UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .disabled(member.isLocked())
            .roles(Role.COMMON).build();
        token = jwtTokenProvider.generateTokenDto(userDto);
        this.mockMvc.perform(TestSnippet.secureGet(url, token.getAccessToken()))
            .andExpect(status().isOk())
            .andExpect(content().json("{result: false}"))
            .andDo(document("PrivilegedFalse"));
    }
}