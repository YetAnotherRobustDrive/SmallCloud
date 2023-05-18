package org.mint.smallcloud.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.QuestionDto;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.repository.AnswerRepository;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.security.jwt.dto.JwtTokenDto;
import org.mint.smallcloud.security.jwt.tokenprovider.JwtTokenProvider;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class BoardControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BoardRepository boardRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/inquiries";

    private UserDetailsDto userDetailsDto;
    private UserDetailsDto adminDto;
    private Member member;
    private Member admin;
    private JwtTokenDto memberToken;
    private JwtTokenDto adminToken;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
        member = Member.createCommon("testMemberName", "testPw", "testNickname");
        admin = Member.createAdmin("testAdminName", "testPw", "testNickname");
        em.persist(member);
        em.persist(admin);
        em.flush();

        userDetailsDto = UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .disabled(member.isLocked())
            .roles(member.getRole())
            .build();
        adminDto = UserDetailsDto.builder()
            .username(admin.getUsername())
            .password(admin.getPassword())
            .disabled(admin.isLocked())
            .roles(admin.getRole())
            .build();
        memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);
        adminToken = jwtTokenProvider.generateTokenDto(adminDto);
    }

    @Nested
    @DisplayName("/inquiries/문의 등록 테스트")
    class Save {
        private final String url = URL_PREFIX;

        private QuestionDto securedQuestionDto;
        private QuestionDto questionDto;
        private QuestionDto noContactQuestionDto;

        @BeforeEach
        void boot() {
            securedQuestionDto = QuestionDto.builder()
                    .title("testTitle")
                .content("testContent")
                .contact("010-1234-5678")
                .writer("testWriter")
                .build();
            questionDto = QuestionDto.builder()
                    .title("testTitle")
                .content("testContent")
                .contact("010-1234-5678")
                .writer("testWriter")
                .build();
            noContactQuestionDto = QuestionDto.builder()
                    .title("testTitle")
                .content("testContent")
                .writer("testWriter")
                .build();

        }

        @DisplayName("정상적인 1:1 문의 등록")
        @Test
        void fineOne() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                    fieldWithPath("title").description("글의 제목을 담고 있습니다."),
                fieldWithPath("content").description("사용자 질문의 내용을 담고 있습니다."),
                fieldWithPath("contact").description("사용자의 연락처를 담고 있습니다."),
                fieldWithPath("writer").description("글의 작성자를 담고 있습니다."));

            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, securedQuestionDto))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    List<Question> questions = questionRepository.findByWriter(member.getUsername());
                    assertFalse(questions.isEmpty());
                    assertEquals(securedQuestionDto.getContent(), questions.get(0).getContent());
                })
                .andDo(document("OneToOneRegisterInquiry", payload));
        }

        @DisplayName("정상적인 로그인 문의 등록")
        @Test
        void findLogin() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, questionDto))
                .andExpect(status().isOk())
                .andDo(document("LoginRegisterInquiry"));
        }

        @DisplayName("연락처가 없는 1:1 문의 등록")
        @Test
        void noContact() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, noContactQuestionDto))
                .andExpect(status().isBadRequest())
                .andDo(document("NoContactInquiry"));
        }

        @DisplayName("잘못된 토큰 - 1:1 문의 등록")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, securedQuestionDto))
                .andExpect(status().isBadRequest())
                .andDo(document("WrongTokenSaveInquiry"));
        }
    }

    @Nested
    @DisplayName("/inquiries/문의 전체 조회 테스트")
    class GetInquiries {
        private final String url = URL_PREFIX;
        private Question question;
        private Question question1;
        private List<Question> findQuestion;

        @BeforeEach
        void boot() {
            question = Question.question("testTitle", "testContent", "testContact");
            question1 = Question.question("testTitle", "testContent1", "testContact1");
            questionRepository.save(question);
            questionRepository.save(question1);
            findQuestion = questionRepository.findAll();
        }

        @DisplayName("정상적인 문의 전체 조회")
        @Test
        void fine() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, adminToken.getAccessToken()))
                .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].title").value(findQuestion.get(0).getTitle()))
                .andExpect(jsonPath("$.[0].content").value(findQuestion.get(0).getContent()))
                .andExpect(jsonPath("$.[0].contact").value(findQuestion.get(0).getContact()))
                    .andExpect(jsonPath("$.[1].title").value(findQuestion.get(1).getTitle()))
                .andExpect(jsonPath("$.[1].content").value(findQuestion.get(1).getContent()))
                .andExpect(jsonPath("$.[1].contact").value(findQuestion.get(1).getContact()))
                .andDo(document("GetAllInquiries"));
        }
    }

    @Nested
    @DisplayName("/inquiries/문의 선택 조회 테스트")
    class select {
        final String url = URL_PREFIX + "/{queryId}";

        @DisplayName("정상적인 문의 선택 조회")
        @Test
        void fine() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact");
            questionRepository.save(question);
            mockMvc.perform(TestSnippet.secured(get(url, question.getId()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(question.getTitle()))
                .andExpect(jsonPath("$.content").value(question.getContent()))
                .andExpect(jsonPath("$.contact").value(question.getContact()))
                .andDo(document("GetOneInquiry",
                    pathParameters(
                        parameterWithName("queryId").description("조회할 문의의 id")
                    )));
        }

        @DisplayName("잘못된 문의 선택 조회")
        @Test
        void wrongSelect() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, 1000), adminToken.getAccessToken()))
                .andExpect(status().isNotFound())
                .andDo(document("CannotFindInquiry"));
        }

        @DisplayName("잘못된 토큰 - 문의 선택 조회")
        @Test
        void wrongToken() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact");
            questionRepository.save(question);
            mockMvc.perform(TestSnippet.secured(get(url, question.getId()), "testWrongToken"))
                .andExpect(status().isBadRequest())
                .andDo(document("WrongTokenGetInquiry"));
        }

    }

    @Nested
    @DisplayName("/inquiries/ 문의 답변 테스트")
    class answer {
        private RequestDto answerDto;

        @BeforeEach
        void boot() {
            answerDto = RequestDto.builder()
                    .title("testTitle")
                    .content("testContent")
                    .build();
        }

        final String url = URL_PREFIX + "/answer";

        @DisplayName("정상적 답변 저장")
        @Test
        void fine() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact");
            Answer answer = Answer.answer("testTitle1", "testContent1", question);
            answerRepository.save(answer);
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, answerDto))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{result: true}"))
//                    .andExpect(jsonPath("$.title").value(board.getTitle()))
//                    .andExpect(jsonPath("$.content").value(board.getContent()))
                    .andDo(document("SaveAnswer"));
        }
    }
}