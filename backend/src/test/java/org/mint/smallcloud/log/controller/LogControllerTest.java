package org.mint.smallcloud.log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
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
    private UserDetailsDto userDetailsDto;
    private Member member;
    private Member member1;


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

        userDetailsDto = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();

        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);

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
}