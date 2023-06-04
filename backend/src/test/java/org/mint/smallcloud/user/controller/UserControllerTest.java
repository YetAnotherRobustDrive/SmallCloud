package org.mint.smallcloud.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.RegisterDto;
import org.mint.smallcloud.user.dto.UserProfileRequestDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    private MockMvc mockMvc;
    private final String URL_PREFIX = "/users";
    private final String DOCUMENT_NAME = "User/{ClassName}/{methodName}";

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
    class Delete {
        final String url = URL_PREFIX + "/{username}/delete";

        @DisplayName("정상 요청")
        @Test
        public void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect((rst) -> assertNull(em.find(Member.class, member1.getId())))
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("username").description("삭제하려는 유저의 아이디")
                    )));
        }


        @DisplayName("잘못된 형태의 유저id")
        @Test
        public void wrongFormat() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, "jfieowoifwejiofjioewjfioewjiof"), adminToken.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("유저가 잘못된 접근")
        @Test
        public void unauthorized() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), memberToken.getAccessToken()))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("없는 유저 삭제 요청")
        @Test
        public void notFindUser() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, "fewfas"), adminToken.getAccessToken()))
                .andExpect(status().isNotFound())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/users docs")
    class Register {
        private final String url = URL_PREFIX;
        private RegisterDto registerDto;

        @BeforeEach
        public void boot() {
            registerDto = RegisterDto.builder()
                .id("user2")
                .password("pw2")
                .name("name")
                .build();
        }

        @DisplayName("정상 요청")
        @Test
        public void ok() throws Exception {

            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, registerDto))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME, requestFields(
                    fieldWithPath("id").description("user id"),
                    fieldWithPath("name").description("user nickname"),
                    fieldWithPath("password").description("user password"))));
        }

        @DisplayName("유저가 잘못된 접근")
        @Test
        public void unauthorized() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, registerDto))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 요청 포멧")
        @Test
        public void wrongFormat() throws Exception {
            registerDto = RegisterDto.builder()
                .name("fdsajklfjkldsajklfjslkdjfaklj")
                .password("fldajsklfjdsklajflkdsajflkdsajfldsj")
                .id("아아아아아아")
                .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, registerDto))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("존재하는 유저 등록 요청")
        @Test
        public void duplicated() throws Exception {
            registerDto = RegisterDto.builder()
                .name(member1.getNickname())
                .id(member1.getUsername())
                .password(member1.getPassword())
                .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, registerDto))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/users/{username}/update docs")
    class Update {
        private final String url = URL_PREFIX + "/{username}/update";
        private UserProfileRequestDto userProfileRequestDto;
        private Group group1;

        @BeforeEach
        public void boot() {
            group1 = Group.of("group1");
            em.persist(group1);
            em.flush();
            userProfileRequestDto = UserProfileRequestDto.builder()
                .username("abc")
                .nickname("def")
                .profileImageLocation(FileLocation.of("testLocation"))
                .build();
        }


        @DisplayName("정상 요청")
        @Test
        public void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), adminToken.getAccessToken(), objectMapper, userProfileRequestDto))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    Member member = em.find(Member.class, member1.getId());
                    assertEquals(member.getUsername(), userProfileRequestDto.getUsername());
                    assertEquals(member.getNickname(), userProfileRequestDto.getNickname());
                })
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("username").description("바꾸려는 id"),
                        fieldWithPath("nickname").description("바꾸려는 nickname"),
                        fieldWithPath("location").description("프로필 이미지 위치")
                    ),
                    pathParameters(
                        parameterWithName("username").description("바꾸려고 하는 유저의 id")
                    )));
        }

        @DisplayName("권한 없는 요청")
        @Test
        void unauthorized() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), memberToken.getAccessToken(), objectMapper, userProfileRequestDto))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 포멧의 요청")
        @Test
        void wrongFormat() throws Exception {
            userProfileRequestDto = UserProfileRequestDto.builder()
                .username("wfoijfijefwijifzzzeijfejfeiwfwj")
                .profileImageLocation(null)
                .build();
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), adminToken.getAccessToken(), objectMapper, userProfileRequestDto))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("없는 유저")
        @Test
        void notFoundUser() throws Exception {
            userProfileRequestDto = UserProfileRequestDto.builder()
                .username("abc")
                .profileImageLocation(null)
                .build();
            mockMvc.perform(TestSnippet.secured(post(url, "abc"), adminToken.getAccessToken(), objectMapper, userProfileRequestDto))
                .andExpect(status().isNotFound())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("중복된 username")
        @Test
        void duplicatedName() throws Exception {
            Member member = Member.of("abc", "pw", "nick");
            em.persist(member);
            em.flush();
            userProfileRequestDto = UserProfileRequestDto.builder()
                .username("abc")
                .profileImageLocation(null)
                .build();
            mockMvc.perform(TestSnippet.secured(post(url, member1.getUsername()), adminToken.getAccessToken(), objectMapper, userProfileRequestDto))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/{username} docs")
    class Profile {
        private final String url = URL_PREFIX + "/{username}";

        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, member1.getUsername()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    UserProfileResponseDto dto = objectMapper.readValue(rst.getResponse().getContentAsString(), UserProfileResponseDto.class);
                    assertEquals(dto.getUsername(), member1.getUsername());
                    assertEquals(dto.getNickname(), member1.getNickname());
                    assertEquals(dto.getGroupName(), member1.getGroupName());
                })
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("username").description("조회하려는 유저의 id")
                    )));
        }

        @DisplayName("권한없는 요청")
        @Test
        void unauthorized() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, member1.getUsername()), memberToken.getAccessToken()))
                .andExpect(status().isForbidden())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("존재하지 않는 유저")
        @Test
        void notFoundUser() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, "abc"), adminToken.getAccessToken()))
                .andExpect(status().isNotFound())
                .andDo(document(DOCUMENT_NAME));
        }


        @DisplayName("잘못된 유저 포멧")
        @Test
        void wrongFormat() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, "fdsafdsafsfasdsafdfdas"), adminToken.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/search docs")
    class Search {
        private final String url = URL_PREFIX
            + "/search?q={q}";

        @BeforeEach
        public void boot() {
            Member member2 = Member.of("user2", "pw", "nick");
            Member member3 = Member.of("user3", "pw", "nick");

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.flush();
        }


        @DisplayName("정상 요청")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, "ser"), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.length()").value(3))
                .andDo(document(DOCUMENT_NAME,
                    requestParameters(
                        parameterWithName("q").description("검색할 닉네임")
                    )));
        }
    }

}