package org.mint.smallcloud.log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        adminToken = jwtTokenProvider.generateTokenDto(userDetailsDto);

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
        void boot(){
            log1 = UserLog.of(member, LocalDateTime.now(), "/ping/success", "111.111.111.111", false);
            log2 = UserLog.of(member, LocalDateTime.now(), "/ping/fail", "111.111.111.111", false);
            log3 = UserLog.of(member1, LocalDateTime.now(), "/ping/success", "111.111.111.111", false);
            log4 = UserLog.of(member1, LocalDateTime.now(), "/ping/fail", "111.111.111.111", false);
            em.persist(log1);
            em.persist(log2);
            em.persist(log3);
            em.persist(log4);
            em.flush();
        }

        @Test
        @DisplayName("로그인 로그 조회")
        public void ok() throws Exception {
            mockMvc.perform(
                            TestSnippet.secured(get(url), memberToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        List<ResponseLogDto> res = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseLogDto.class));
                        assertEquals(2, res.size());
                        assertEquals(res.get(0).getNickName(), member.getNickname());
                        assertEquals(res.get(0).getAction().startsWith("/ping"), true);
                        assertEquals(res.get(1).getNickName(), member.getNickname());
                        assertEquals(res.get(1).getAction().startsWith("/ping"), true);

                    })
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/logs/admin document")
    class admin {
        private final String url = URL_PREFIX + "/admin";
        private UserLog log1;
        private UserLog log2;
        private UserLog log3;
        private UserLog log4;
        private RequestLogDto requestLogDto;
        private RequestLogDto requestLogDto1;
        @BeforeEach
        void boot() {
            log1 = UserLog.of(admin, LocalDateTime.now(), "/ping/success", "123.123.123.123", true);
            em.persist(log1);
            log2 = UserLog.of(member, LocalDateTime.now(), "/ping/fail", "111.111.111.111", true);
            em.persist(log2);
            log3 = UserLog.of(member1, LocalDateTime.now(), "/ping/pong", "123.123.123.123",false);
            em.persist(log3);
            /**
             * 1. nickName
             * 2. action
             * 3. status true
             * 4. status false
             * 5. startTime
             * 6. endTime
             * 7. startTime, endTime
             * 8. nickName, action
             * 9. nickName, status true
             * 10. nickName, between
             */

            requestLogDto = RequestLogDto.builder()
                    .nickName(member.getNickname())
                    .build();

            requestLogDto1 = RequestLogDto.builder()
                    .action("/ping/success")
                    .build();
        }

        @DisplayName("정상적인 로그 조회")
        @Test
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url), adminToken.getAccessToken(), objectMapper, requestLogDto))
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
    }
}