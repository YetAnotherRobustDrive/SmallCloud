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
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.dto.QuestionDto;
import org.mint.smallcloud.board.dto.RequestDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mint.smallcloud.board.domain.BoardType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/inquiries";
    private final String DOCUMENT_NAME = "Board/{ClassName}/{methodName}";

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
                    .answerId(null)
                .build();
            questionDto = QuestionDto.builder()
                .title("testTitle")
                .content("testContent")
                .contact("010-1234-5678")
                .writer("testWriter")
                    .answerId(null)
                .build();
            noContactQuestionDto = QuestionDto.builder()
                .title("testTitle")
                .content("testContent")
                .writer("testWriter")
                    .answerId(null)
                .build();

        }

        @DisplayName("정상적인 1:1 문의 등록")
        @Test
        void okOneToOne() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                    fieldWithPath("id").description("글의 id를 담고 있습니다."),
                fieldWithPath("title").description("글의 제목을 담고 있습니다."),
                fieldWithPath("content").description("사용자 질문의 내용을 담고 있습니다."),
                fieldWithPath("contact").description("사용자의 연락처를 담고 있습니다."),
                fieldWithPath("writer").description("글의 작성자를 담고 있습니다."),
                    fieldWithPath("answerId").description("질문의 대한 답변의 내용을 담고 있습니다."));

            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, securedQuestionDto))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    List<Question> questions = questionRepository.findByWriter(member.getUsername());
                    assertFalse(questions.isEmpty());
                    assertEquals(securedQuestionDto.getContent(), questions.get(0).getContent());
                })
                .andDo(document(DOCUMENT_NAME, payload));
        }

        @DisplayName("정상적인 로그인 문의 등록")
        @Test
        void okLogin() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, questionDto))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("연락처가 없는 1:1 문의 등록")
        @Test
        void noContact() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, noContactQuestionDto))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰 - 1:1 문의 등록")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, securedQuestionDto))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
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
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value(findQuestion.get(0).getTitle()))
                .andExpect(jsonPath("$.[0].content").value(findQuestion.get(0).getContent()))
                .andExpect(jsonPath("$.[0].contact").value(findQuestion.get(0).getContact()))
                .andExpect(jsonPath("$.[1].title").value(findQuestion.get(1).getTitle()))
                .andExpect(jsonPath("$.[1].content").value(findQuestion.get(1).getContent()))
                .andExpect(jsonPath("$.[1].contact").value(findQuestion.get(1).getContact()))
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/inquiries/문의 선택 조회 테스트")
    class select {
        final String url = URL_PREFIX + "/{queryId}";

        @DisplayName("정상적인 문의 선택 조회")
        @Test
        void ok() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact");
            questionRepository.save(question);
            mockMvc.perform(TestSnippet.secured(get(url, question.getId()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(question.getTitle()))
                .andExpect(jsonPath("$.content").value(question.getContent()))
                .andExpect(jsonPath("$.contact").value(question.getContact()))
                .andDo(document(DOCUMENT_NAME,
                    pathParameters(
                        parameterWithName("queryId").description("조회할 문의의 id")
                    )));
        }

        @DisplayName("잘못된 문의 선택 조회")
        @Test
        void wrongSelect() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, 1000), adminToken.getAccessToken()))
                .andExpect(status().isNotFound())
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰 - 문의 선택 조회")
        @Test
        void wrongToken() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact");
            questionRepository.save(question);
            mockMvc.perform(TestSnippet.secured(get(url, question.getId()), "testWrongToken"))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }

    }

    @Nested
    @DisplayName("/inquiries/ 문의 답변 테스트")
    class answer {
        private RequestDto requestDto;
        Question question;
        Answer answer;
        Member member;
        @BeforeEach
        void boot() {
            requestDto = RequestDto.builder()
                .content("testContent")
                .questionId(1L)
                .build();
            member = Member.createCommon("testWriter", "testPassword", "testNickname");
            em.persist(member);
            question = Question.question("testTitle", "testContent", "testContact", "testWriter", null);
            em.persist(question);
            answer = Answer.answer("testContent1", question);
            em.persist(answer);
            em.flush();
        }


        final String url = URL_PREFIX + "/answer";

        @DisplayName("정상적 1:1 문의 답변 저장")
        @Test
        void okOneToOneAnswer() throws Exception {

            requestDto = RequestDto.builder()
                .content("testContent1")
                .questionId(question.getId())
                .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, requestDto))
                    .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{result: true}"))
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("정상적 로그인 문의 답변 저장")
        @Test
        void okLoginAnswer() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact", null, null);
            questionRepository.save(question);
            Answer answer = Answer.answer("testContent1", question);
            answerRepository.save(answer);
            requestDto = RequestDto.builder()
                .content("testContent1")
                .questionId(question.getId())
                .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, requestDto))
                .andExpect(status().isOk())
                .andExpect(content().json("{result:true}"))
                .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("답변 내용이 없는 답변 저장")
        @Test
        void noContent() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact", "testWriter", null);
            questionRepository.save(question);
            Answer answer = Answer.answer(null, question);
            answerRepository.save(answer);
            requestDto = RequestDto.builder()
                    .content(null)
                    .questionId(question.getId())
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, requestDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }

        @DisplayName("잘못된 토큰")
        @Test
        void wrongToken() throws Exception {
            Question question = Question.question("testTitle", "testContent", "testContact", null, null);
            questionRepository.save(question);
            Answer answer = Answer.answer("testContent1", question);
            answerRepository.save(answer);
            requestDto = RequestDto.builder()
                    .content("testContent1")
                    .questionId(question.getId())
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, "testToken", objectMapper, requestDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/inquiries/questioned 답변되지 않은 문의 조회 테스트")
    class noAnswer {
        final String url = URL_PREFIX + "/questioned";
        private Question question;
        private Question question1;
        private Answer answer;

        private List<Question> findQuestion;

        @BeforeEach
        void boot() {
            answer = Answer.answer("testContent", null);
            answerRepository.save(answer);
            question = Question.question("testTitle", "testContent", "testContact", "testWriter", answer);
            question1 = Question.question("testTitle", "testContent", "testContact", "testWriter", null);
            questionRepository.save(question);
            questionRepository.save(question1);
            findQuestion = questionRepository.findByAnswer(null);
        }

        @Test
        @DisplayName("정상적 문의 조회")
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value(findQuestion.get(0).getTitle()))
                .andExpect(jsonPath("$.[0].content").value(findQuestion.get(0).getContent()))
                .andExpect(jsonPath("$.[0].contact").value(findQuestion.get(0).getContact()))
                .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, findQuestion), "testWrongToken"))
                .andExpect(status().isBadRequest())
                .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/Inquiries/myQuestions 내 문의 내역 전부 가져오기")
    class myQuestions {
        final String url = URL_PREFIX + "/myQuestions";
        private Question question;
        private Question question1;
        private Answer answer;
        private List<Question> findQuestion;

        @BeforeEach
        void boot() {
            answer = Answer.answer("testContent", null);
            answerRepository.save(answer);
            question = Question.question("testTitle", "testContent", "testContact", "testWriter", answer);
            question1 = Question.question("testTitle", "testContent", "testContact", "testWriter", null);
            questionRepository.save(question);
            questionRepository.save(question1);
            findQuestion = questionRepository.findByWriter("testWriter");
        }

        @Test
        @DisplayName("정상적 문의 조회")
        void ok() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url).param("writer", "testWriter"), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME,requestParameters(
                            parameterWithName("writer").description("조회할 문의들의 작성자(로그인)")
                    )));
        }

        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, findQuestion), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/Inquiries/saveBoard 보드 등록")
    class saveBoard {
        final String url = URL_PREFIX + "/board";

        @Test
        @DisplayName("정상적 보드 등록")
        void ok() throws Exception {
            BoardDto boardDto = BoardDto.builder()
                    .title("testTitle")
                    .content("testContent")
                    .boardType(faq)
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, boardDto))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{result: true}"))
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            BoardDto boardDto = BoardDto.builder()
                    .title("testTitle")
                    .content("testContent")
                    .boardType(faq)
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, boardDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("보드 내용 생략")
        void noContent() throws Exception {
            BoardDto boardDto = BoardDto.builder()
                    .title("testTitle")
                    .boardType(faq)
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, boardDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("보드타입 내용 생략")
        void noType() throws Exception {
            BoardDto boardDto = BoardDto.builder()
                    .title("testTitle")
                    .content("testContent")
                    .build();
            mockMvc.perform(TestSnippet.securePost(url, adminToken.getAccessToken(), objectMapper, boardDto))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/Inquiries/board 보드 조회")
    class getBoard {
        final String url = URL_PREFIX + "/board";
        Board board;
        Board board1;
        List<Board> findFAQ;

        @BeforeEach
        void boot() {
            board = Board.board("testTitle", "testContent", faq);
            board1 = Board.board("testTitle1", "testContent1", announcement);
            boardRepository.save(board);
            boardRepository.save(board1);
            findFAQ = boardRepository.findByBoardType(faq);
        }

        @Test
        @DisplayName("정상적 보드 조회 admin")
        void okAdmin() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url).param("boardType", faq.name()), adminToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME, requestParameters(
                            parameterWithName("boardType").description("조회할 보드 타입")
                    )));
        }

        @Test
        @DisplayName("정상적 보드 조회 common")
        void okCommon() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url).param("boardType", faq.name()), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, findFAQ), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/Inquiries/board 보드 날짜순 2개 조회")
    class getBoardCreatedDate {
        final String url = URL_PREFIX + "/board/created";
        Board board;
        Board board1;
        Board board2;
        List<Board> findTerms;
        List<Board> findPrivacy;
        MultiValueMap<String, String> info;
        MultiValueMap<String, String> info1;
        @BeforeEach
        void boot() {
            board = Board.board("testTitle", "testContent", terms);
            board1 = Board.board("testTitle1", "testContent1", terms);
            board2 = Board.board("testTitle2","testContent2", privacy);
            boardRepository.save(board);
            boardRepository.save(board1);
            boardRepository.save(board2);
            findTerms = boardRepository.findTop2ByBoardTypeOrderByCreatedDateDesc(terms);
            findPrivacy = boardRepository.findTop2ByBoardTypeOrderByCreatedDateDesc(privacy);
            info = new LinkedMultiValueMap<>();
            info.add("boardType", terms.name());
            info1 = new LinkedMultiValueMap<>();
            info1.add("boardType", privacy.name());
        }

        @Test
        @DisplayName("정상적 첫번째 보드 조회 admin")
        void okAdminFirst() throws Exception {
            info.add("createdDate", "0");
            mockMvc.perform(TestSnippet.secured(get(url).params(info), adminToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME,requestParameters(
                            parameterWithName("boardType").description("조회할 보드 타입"),
                            parameterWithName("createdDate").description("0번이 최근, 1번이 직전 보드")
                    )));
        }

        @Test
        @DisplayName("정상적 두번째 보드 조회 admin")
        void okAdminSecond() throws Exception {
            info.add("createdDate", "1");
            mockMvc.perform(TestSnippet.secured(get(url).params(info), adminToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("정상적 첫번째 보드 조회 common")
        void okCommonFirst() throws Exception {
            info.add("createdDate", "0");
            mockMvc.perform(TestSnippet.secured(get(url).params(info), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME));
        }

        @Test
        @DisplayName("정상적 두번째 보드 조회 common")
        void okCommonSecond() throws Exception {
            info.add("createdDate", "1");
            mockMvc.perform(TestSnippet.secured(get(url).params(info), memberToken.getAccessToken()))
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 3번째 보드 조회")
        void thirdBoard() throws Exception {
            info.add("createdDate", "2");
            mockMvc.perform(TestSnippet.secured(get(url).params(info), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("없는 보드 조회")
        void noBoard() throws Exception {
            info1.add("createdDate", "1");
            mockMvc.perform(TestSnippet.secured(get(url).params(info1), memberToken.getAccessToken()))
                    .andExpect(status().isNotFound())
                    .andDo(document(DOCUMENT_NAME));
        }
        @Test
        @DisplayName("잘못된 토큰")
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, findTerms), "testWrongToken"))
                    .andExpect(status().isBadRequest())
                    .andDo(document(DOCUMENT_NAME));
        }
    }

    @Nested
    @DisplayName("/inquiries/search/answer docs")
    class searchAnswer {
        String url = URL_PREFIX + "/search/answer?answerId={answerId}";
        private Question question;
        private Question question1;
        private Answer answer;
        private Answer findAnswer;

        @BeforeEach
        void boot() {
            answer = Answer.answer("testContent", null);
            em.persist(answer);
            question = Question.question("testTitle", "testContent", "testContact", "testWriter", answer);
            em.persist(question);
            question1 = Question.question("testTitle", "testContent", "testContact", "testWriter", null);
            em.persist(question1);
        }

        @DisplayName("정상적 답변 조회")
        @Test
        void ok() throws Exception {
            findAnswer = answerRepository.findByQuestionId(question.getId()).orElseThrow();
            mockMvc.perform(TestSnippet.secured(get(url, findAnswer.getId()), adminToken.getAccessToken()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document(DOCUMENT_NAME, requestParameters(
                            parameterWithName("answerId").description("조회할 답변 아이디를 담고 있습니다.")
                    )));
        }
    }
}