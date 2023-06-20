package org.mint.smallcloud.log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLoginLogDto;
import org.mint.smallcloud.log.user.UserLog;
import org.mint.smallcloud.log.user.UserLogRepository;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mint.smallcloud.TestSnippet.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class LogControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @PersistenceContext
    private EntityManager em;
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/logs";
    private final String DOCUMENT_NAME = "log/{ClassName}/{methodName}";
    private JwtTokenDto memberToken;
    private JwtTokenDto adminToken;
    private UserDetailsDto userDetailsDto;
    private UserDetailsDto userDetailsDto1;
    private Member member;
    private Member member1;
    private Member admin;


    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserLogRepository userLogRepository;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        member = Member.createCommon("testMemberName", "testPw", "testNickname");
        em.persist(member);
        member1 = Member.createCommon("testMemberName1", "testPw1", "testNickname1");
        em.persist(member1);
        em.flush();
        admin = Member.createAdmin("admin", "admin", "admin");
        em.persist(admin);
        em.flush();

        userDetailsDto = UserDetailsDto.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.isLocked())
                .roles(admin.getRole())
                .build();

        userDetailsDto1 = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();

        adminToken = jwtTokenProvider.generateTokenDto(userDetailsDto);
        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto1);

    }

    @Nested
    @DisplayName("/logs document")
    class UserLoginLogs {
        private final String url = URL_PREFIX;
        private UserLog log1;
        private UserLog log2;
        private UserLog log3;
        private UserLog log4;

        @BeforeEach
        void boot() {
            log1 = UserLog.of(member, LocalDateTime.now(), "/ping/login/" + member.getUsername() + "/success", "111.111.111.111", false);
            log2 = UserLog.of(member, LocalDateTime.now(), "/ping/login/" + member.getUsername() + "/fail", "111.111.111.111", false);
            log3 = UserLog.of(member1, LocalDateTime.now(), "/ping/login/" + member1.getUsername() + "/success", "111.111.111.111", false);
            log4 = UserLog.of(member1, LocalDateTime.now(), "/ping/login/" + member1.getUsername() + "/fail", "111.111.111.111", false);
            em.persist(log1);
            em.persist(log2);
            em.persist(log3);
            em.persist(log4);
            em.flush();
        }

        @Test
        @DisplayName("로그인 로그 조회")
        public void ok() throws Exception {
            ResponseLoginLogDto expected1 = ResponseLoginLogDto.builder()
                    .localDateTime(log1.getTime())
                    .action("login/" + member.getUsername() + "/")
                    .ipAddr(log1.getIpAddr())
                    .status(true)
                    .build();
            ResponseLoginLogDto expected2 = ResponseLoginLogDto.builder()
                    .localDateTime(log2.getTime())
                    .action("login/" + member.getUsername() + "/")
                    .ipAddr(log2.getIpAddr())
                    .status(false)
                    .build();
            mockMvc.perform(
                            TestSnippet.secured(get(url), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        List<ResponseLoginLogDto> res = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseLoginLogDto.class));
                        for (ResponseLoginLogDto dto : res) {
                            System.out.println(dto.getLocalDateTime());
                            System.out.println(dto.getAction());
                            System.out.println(dto.getIpAddr());
                            System.out.println(dto.getStatus());
                        }
                        assertEquals(2, res.size());
                        assertEquals(expected1.getAction(), res.get(0).getAction());
                        assertEquals(expected1.getIpAddr(), res.get(0).getIpAddr());
                        assertEquals(expected1.getLocalDateTime(), res.get(0).getLocalDateTime());
                        assertEquals(expected1.getStatus(), res.get(0).getStatus());

                        assertEquals(expected2.getAction(), res.get(1).getAction());
                        assertEquals(expected2.getIpAddr(), res.get(1).getIpAddr());
                        assertEquals(expected2.getLocalDateTime(), res.get(1).getLocalDateTime());
                        assertEquals(expected2.getStatus(), res.get(1).getStatus());

                    })
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/logs/admin document")
    class admin {
        private final String url = URL_PREFIX + "/admin";
        private UserLog log;
        private RequestLogDto requestLogDto;

        @BeforeEach
        void boot() {
            List<String> actionList = new ArrayList<>();
            actionList.addAll(Arrays.asList( //24개
                    "/auth/register",
                    "/auth/register",
                    "/auth/register",
                    "/auth/login",
                    "/auth/login",
                    "/auth/login",
                    "/auth/refresh",
                    "/auth/refresh",
                    "/auth/refresh",
                    "/auth/elevate",
                    "/auth/elevate",
                    "/auth/elevate",
                    "/files/1",
                    "/files/2",
                    "/files/3",
                    "/admin/lock",
                    "/admin/lock",
                    "/admin/lock",
                    "/admin/unlock",
                    "/admin/unlock",
                    "/admin/unlock",
                    "/group/test/add-user/user1",
                    "/group/test/add-user/user1",
                    "/group/test/add-user/user1"
            ));
            LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
            for (int i = 0; i < actionList.size(); i++) { //사용자 당 48개의 로그 생성
                log = UserLog.of(member, testTime.plusHours(i), actionList.get(i), "111.111.111.111", true);
                em.persist(log);
                log = UserLog.of(member, testTime.plusHours(i), actionList.get(i), "111.111.111.111", false);
                em.persist(log);
                log = UserLog.of(member1, testTime.plusHours(i), actionList.get(i), "111.111.222.111", true);
                em.persist(log);
                log = UserLog.of(member1, testTime.plusHours(i), actionList.get(i), "111.111.222.111", false);
                em.persist(log);
            }
            em.flush();
            /**
             * 1. nickName
             *  - 24개 * 2개(t/f) = 48개
             * 2. action
             *  - 3개 * 2개(t/f) * 2개(2명) = 12개
             * 3. status true
             *  - 24개 * 2개(2명) = 48개
             * 4. status false
             * - 24개 * 2개(2명) = 48개
             * 5. startTime
             * 6. endTime
             * 7. startTime, endTime
             * 8. nickName, action
             * 9. nickName, status true
             * 10. nickName, between
             */
        }

        @DisplayName("닉네임 조회")
        @Test
        void nicknameFilter() throws Exception {

            requestLogDto = RequestLogDto.builder()
                    .nickName(member.getNickname())
                    .build();
            Pageable pageable = PageRequest.of(0, 5);
            Page<UserLog> userLogs = userLogRepository.findLogs(requestLogDto.getNickName(), null, null, null, null, pageable);
            System.out.println(userLogs.getSize());
            System.out.println(userLogs.getTotalPages());
            System.out.println(userLogs.getTotalElements());
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME, requestFields(
                            fieldWithPath("nickName").description("닉네임"),
                            fieldWithPath("action").description("액션"),
                            fieldWithPath("status").description("상태"),
                            fieldWithPath("startTime").description("시작시간"),
                            fieldWithPath("endTime").description("종료시간")
                    )));
        }

        @DisplayName("액션 조회")
        @Test
        void actionFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .action("/auth/register")
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("상태 조회")
        @Test
        void statusFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .status(true)
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("시작시간 조회")
        @Test
        void startTimeFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .startTime(LocalDateTime.of(2023, 1, 1, 2, 59, 59, 0))
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("종료시간 조회")
        @Test
        void endTimeFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .endTime(LocalDateTime.of(2023, 1, 1, 2, 59, 59, 0))
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("시작시간, 종료시간 조회")
        @Test
        void startTimeAndEndTimeFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .startTime(LocalDateTime.of(2023, 1, 1, 0, 59, 59, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 2, 59, 59, 0))
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("닉네임, 액션 조회")
        @Test
        void nicknameAndActionFilter() throws Exception {

            requestLogDto = RequestLogDto.builder()
                    .nickName(member.getNickname())
                    .action("/files/{id}")
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("닉네임, 상태 조회")
        @Test
        void nicknameAndStatusFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .nickName(member.getNickname())
                    .status(false)
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("닉네임, 시작시간, 종료시간 조회")
        @Test
        void nicknameAndStartTimeAndEndTimeFilter() throws Exception {
            requestLogDto = RequestLogDto.builder()
                    .nickName(member.getNickname())
                    .startTime(LocalDateTime.of(2023, 1, 1, 0, 59, 59, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 2, 59, 59, 0))
                    .build();
            mockMvc.perform(TestSnippet.secured(post(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }
        

        @DisplayName("페이지 조회")
        @Test
        void pageFilter() throws Exception {
            String urlPaging = url + "?size=10&page=9";
            requestLogDto = RequestLogDto.builder()
                    .build(); // find all

            mockMvc.perform(TestSnippet.secured(post(urlPaging), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("파라미터 있는 경우")
        @Test
        void parameterExist() throws Exception {
            String urlPaging = url + "?size=100&page=0";
            requestLogDto = RequestLogDto.builder()
                    .action("/group/{groupName}/add-user/{username}")
                    .build();


            mockMvc.perform(TestSnippet.secured(post(urlPaging), adminToken.getAccessToken(), objectMapper, requestLogDto))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andDo(document(DOCUMENT_NAME));
        }
    }
}