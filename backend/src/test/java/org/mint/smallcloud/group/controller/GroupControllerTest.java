package org.mint.smallcloud.group.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.group.domain.Group;
import org.mint.smallcloud.group.dto.GroupRequestDto;
import org.mint.smallcloud.group.dto.GroupTreeDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mint.smallcloud.TestSnippet.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class GroupControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/group";
    private final String DOCUMENT_NAME = "Group/{ClassName}/{methodName}";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    private EntityManager em;

    private Member admin;
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
        em.persist(admin);
        em.flush();
        adminToken = jwtTokenProvider.generateTokenDto(UserDetailsDto.builder()
            .username(admin.getUsername())
            .password(admin.getPassword())
            .roles(admin.getRole())
            .disabled(false).build());
    }

    @Nested
    @DisplayName("/create docs")
    class Create {
        final String url = URL_PREFIX + "/create";
        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            GroupRequestDto dto = GroupRequestDto.builder()
                    .groupName("test").build();
            mockMvc.perform(TestSnippet.secured(post(url, objectMapper, dto), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> assertThat(em.createQuery("select g from Group g where g.name = 'test'", Group.class)
                        .getResultList().size()).isEqualTo(1)
                );
            dto = GroupRequestDto.builder()
                .groupName("test1")
                .parentName("test")
                .build();
            mockMvc.perform(TestSnippet.secured(post(url, objectMapper, dto), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> {
                        Group group = em.createQuery("select g from Group g where g.name = 'test1'", Group.class)
                            .getSingleResult();
                        assertThat(group.getName()).isEqualTo("test1");
                        assertThat(group.getParentGroup().getName()).isEqualTo("test");
                    }
                )
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("groupName").description("만들 그룹 이름"),
                        fieldWithPath("parentName").description("부모 그룹 이름").optional()
                    )));
        }
    }

    @Nested
    @DisplayName("/{groupName}/delete")
    class Delete {
        final String url = URL_PREFIX + "/{groupName}/delete";
        @BeforeEach
        void boot() {
            Group group = Group.of("test");
            em.persist(group);
            em.flush();
        }
        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, "test"),
                    adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> assertThat(em.createQuery("select g from Group g where g.name = 'test'", Group.class)
                        .getResultList().size()).isEqualTo(0)
                )
                .andDo(document(DOCUMENT_NAME, pathParameters(
                    parameterWithName("groupName").description("삭제할 그룹 이름")
                )));
        }
    }

    @Nested
    @DisplayName("/{groupName}/add-user/{username}")
    class AddUser {

        String url = URL_PREFIX + "/{groupName}/add-user/{username}";
        Member user1;
        Group group;

        @BeforeEach
        void boot() {
            group = Group.of("test");
            user1 = Member.createCommon("user1", "user1", "user1");
            em.persist(group);
            em.persist(user1);
            em.flush();
        }

        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, group.getName(), user1.getUsername()),
                    adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> assertThat(em.createQuery("select m from Member m where m.username = ?1", Member.class)
                        .setParameter(1, user1.getUsername())
                        .getSingleResult().getGroup().getName()).isEqualTo(group.getName())
                )
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("groupName").description("추가할 그룹 이름"),
                        parameterWithName("username").description("추가할 유저 이름")
                    )));
        }
    }

    @Nested
    @DisplayName("/{groupName}/update")
    class Update {
        String url = URL_PREFIX + "/{groupName}/update";
        Group group;
        Group parent;
        @BeforeEach
        void boot() {
            group = Group.of("test");
            parent = Group.of("parent");
            em.persist(group);
            em.persist(parent);
            em.flush();
        }
        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            GroupRequestDto dto = GroupRequestDto.builder()
                .parentName(parent.getName())
                .build();
            mockMvc.perform(TestSnippet.secured(post(url, objectMapper, dto, group.getName()),
                    adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    (ignore) -> {
                        Group rst = em.createQuery("select g from Group g where g.name = ?1", Group.class)
                            .setParameter(1, group.getName())
                            .getSingleResult();
                        assertThat(rst.getName()).isEqualTo(group.getName());
                        assertThat(rst.getParentGroup().getName()).isEqualTo(parent.getName());
                    }
                );
            String newName = group.getName() + "a";
            dto = GroupRequestDto.builder()
                    .groupName(newName).build();
            mockMvc.perform(TestSnippet.secured(post(url, objectMapper, dto, group.getName()),
                    adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (ignore) -> {
                        Group rst = em.createQuery("select g from Group g where g.name = ?1", Group.class)
                            .setParameter(1, newName)
                            .getSingleResult();
                        assertThat(rst.getName()).isEqualTo(newName);
                        assertThat(rst.getParentGroup().getName()).isEqualTo(parent.getName());
                    }
                )
                .andDo(document(DOCUMENT_NAME,
                    requestFields(
                        fieldWithPath("groupName").description("만들 그룹 이름").optional(),
                        fieldWithPath("parentName").description("부모 그룹 이름").optional()
                    )));
        }
    }

    @Nested
    @DisplayName("/{groupName}/delete-user/{username}")
    class DeleteUser {
        String url = URL_PREFIX + "/{groupName}/delete-user/{username}";
        Group group;
        Member user1;
        @BeforeEach
        void boot() {
            group = Group.of("test");
            user1 = Member.createCommon("user1", "user1", "user1");
            group.addMember(user1);
            em.persist(group);
            em.persist(user1);
            em.flush();
        }
        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(post(url, group.getName(), user1.getUsername()),
                    adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    (ignore) -> assertThat(em.createQuery("select m from Member m where m.username = ?1", Member.class)
                        .setParameter(1, user1.getUsername())
                        .getSingleResult().getGroup()).isNull()
                )
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("groupName").description("그룹 아이디"),
                        parameterWithName("username").description("유저 아이디")
                    )));
        }
    }

    @Nested
    @DisplayName("/{groupName}")
    class GroupTree {
        String url = URL_PREFIX + "/{groupName}";
        Group group1;
        Group group2;
        Group group3;
        Group parent;

        @BeforeEach
        void boot() {
            group1 = Group.of("group1");
            group2 = Group.of("group2");
            group3 = Group.of("group3");

            parent = Group.of("parent");
            em.persist(parent);
            em.persist(group1);
            em.persist(group2);
            em.persist(group3);
            em.flush();
            group1.setParentGroup(parent);
            group2.setParentGroup(parent);
            em.flush();
        }

        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, group3.getName()),
                    adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> {
                        GroupTreeDto dto = objectMapper.readValue(rst.getResponse().getContentAsString(), GroupTreeDto.class);
                        assertThat(dto.getName()).isEqualTo(group3.getName());
                        assertThat(dto.getSubGroups()).isEmpty();
                    }
                );
            mockMvc.perform(TestSnippet.secured(get(url, parent.getName()),
                    adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> {
                        GroupTreeDto dto = objectMapper.readValue(rst.getResponse().getContentAsString(), GroupTreeDto.class);
                        assertThat(dto.getName()).isEqualTo(parent.getName());
                        List<String> collect = dto.getSubGroups().stream().map(GroupTreeDto::getName)
                            .collect(Collectors.toList());
                        assertThat(collect).contains(group1.getName(), group2.getName());
                    }
                )
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("groupName").description("그룹 아이디")
                    )));
        }
    }

    @Nested
    @DisplayName("/{groupName}/user-list")
    class UserList {
        String url = URL_PREFIX + "/{groupName}/user-list";
        Group group;
        Member user1;
        Member user2;
        @BeforeEach
        void boot() {
            group = Group.of("test");
            user1 = Member.createCommon("user1", "user1", "user1");
            user2 = Member.createCommon("user2", "user2", "user2");
            em.persist(group);
            em.persist(user1);
            em.persist(user2);
            em.flush();
            group.addMember(user1);
            group.addMember(user2);
            em.flush();
        }
        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, group.getName()),
                    adminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> {
                        List<UserProfileResponseDto> dtos = objectMapper.readValue(rst.getResponse().getContentAsString(),
                            new TypeReference<List<UserProfileResponseDto>>() {});
                        assertThat(dtos).hasSize(2);
                        assertThat(dtos.stream().map(UserProfileResponseDto::getUsername).collect(Collectors.toList()))
                            .contains(user1.getUsername(), user2.getUsername());
                    }
                )
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("groupName").description("그룹 아이디")
                    )));
        }
    }

    @Nested
    @DisplayName("/search")
    class Search {
        String url = URL_PREFIX + "/search";
        Group group1;
        Group group2;
        Group group3;

        @BeforeEach
        void boot() {
            group1 = Group.of("group1");
            group2 = Group.of("group2");
            group3 = Group.of("group3");

            em.persist(group1);
            em.persist(group2);
            em.persist(group3);
            em.flush();
        }

        @DisplayName("ok")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url).param("q", "roup"),
                    adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(
                    (rst) -> {
                        List<String> dtos = objectMapper.readValue(rst.getResponse().getContentAsString(),
                            new TypeReference<List<String>>() {});
                        assertThat(dtos).hasSize(3);
                        assertThat(dtos).contains(group1.getName(), group2.getName(), group3.getName());
                    }
                )
                .andDo(document(DOCUMENT_NAME,
                    requestParameters(
                        parameterWithName("q").description("검색어")
                    )));
        }
    }
}