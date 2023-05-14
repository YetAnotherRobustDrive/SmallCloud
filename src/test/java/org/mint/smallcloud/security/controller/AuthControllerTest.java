package org.mint.smallcloud.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private LoginDto user1;
    private LoginDto user2;
    private LoginDto user3;

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
        user3 = LoginDto.builder()
            .id("user3")
            .password("pw3")
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
            .andExpect((result) -> assertNotNull(memberRepository.findByUsername(registerDto.getId()).orElse(null)))
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
        LoginDto wrongPasswordLoginDto1 = LoginDto.builder()
            .id(user1.getId())
            .password(user1.getPassword() + "abc")
            .build();
        LoginDto notValidDto = LoginDto.builder()
            .id("fjiowejfioewjoifjweoijf@@@ioewjofijewiofjewiojioewj")
            .password("fjioewjifojewiofjewiojfioewjfioewjiofjwe")
            .build();
        memberRepository.save(Member.of(user1.getId(),
            user1.getPassword(), "nickname"));

        this.mockMvc.perform(TestSnippet.post(url, objectMapper, user1))
            .andExpect(status().isOk())
            .andDo(document("Login", payload));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, user2))
            .andExpect(status().isForbidden())
            .andDo(document("NotFoundUserLogin", payload));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, wrongPasswordLoginDto1))
            .andExpect(status().isForbidden())
            .andDo(document("WrongPassword", payload));
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, notValidDto))
            .andExpect(status().isBadRequest())
            .andDo(document("NotValidLogin"));
    }

    @Test
    @DisplayName("/auth/elevate document")
    public void elevate() throws Exception {
        final String url = URL_PREFIX + "/elevate";
        RequestFieldsSnippet payload = requestFields(
            fieldWithPath("password").description("user password"));
        Map<String, String> map = new HashMap<>();
        map.put("password", user1.getPassword());
        memberRepository.save(Member.of(user1.getId(),
            user1.getPassword(), "nickname"));
        JwtTokenDto token = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(user1.getId())
                .password(user1.getPassword())
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

        memberRepository.save(Member.of(user1.getId(),
            user1.getPassword(), "nickname"));
        memberRepository.save(Member.of(user2.getId(),
            user2.getPassword(), "nickname"));

        JwtTokenDto token = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(user1.getId())
                .password(user1.getPassword())
                .roles(Role.COMMON)
                .disabled(false)
                .build());
        Map<String, String> map = new HashMap<>();
        map.put("password", user1.getPassword());
        JwtTokenDto privilegeToken = objectMapper.readValue(
            this.mockMvc.perform(TestSnippet.securePost(URL_PREFIX + "/elevate", token.getAccessToken(), objectMapper, map))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
            , JwtTokenDto.class);

        this.mockMvc.perform(TestSnippet.securePost(url, privilegeToken.getAccessToken()))
            .andExpect(status().isOk())
            .andExpect((rst) -> assertNull(memberRepository.findByUsername(user1.getId()).orElse(null)))
            .andDo(document("Deregister"));


        JwtTokenDto commonToken = jwtTokenProvider.generateTokenDto(
            UserDetailsDto.builder()
                .username(user2.getId())
                .password(user2.getPassword())
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
                )));

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