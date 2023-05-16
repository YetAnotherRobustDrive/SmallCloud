package org.mint.smallcloud.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.repository.BoardRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

        private BoardDto securedBoardDto;
        private BoardDto boardDto;
        private BoardDto notContactBoardDto;

        @BeforeEach
        void boot() {
            securedBoardDto = BoardDto.builder()
                .content("testContent")
                .contact("010-1234-5678")
                .writer("testWriter")
                .build();
            boardDto = BoardDto.builder()
                .content("testContent")
                .contact("010-1234-5678")
                .writer("testWriter")
                .build();
            notContactBoardDto = BoardDto.builder()
                .content("testContent")
                .writer("testWriter")
                .build();

        }

        @DisplayName("정상적인 1:1 문의 등록")
        @Test
        void fineOne() throws Exception {
            RequestFieldsSnippet payload = requestFields(
                fieldWithPath("content").description("사용자 질문의 내용을 담고 있습니다."),
                fieldWithPath("contact").description("사용자의 연락처를 담고 있습니다."),
                fieldWithPath("writer").description("글의 작성자를 담고 있습니다."));

            mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, securedBoardDto))
                .andExpect(status().isOk())
                .andExpect((rst) -> {
                    List<Board> boards = boardRepository.findByWriter(member.getUsername());
                    assertFalse(boards.isEmpty());
                    assertEquals(securedBoardDto.getContent(), boards.get(0).getContent());
                })
                .andDo(document("OneToOneRegisterInquiry", payload));
        }

        @DisplayName("정상적인 로그인 문의 등록")
        @Test
        void findLogin() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, boardDto))
                .andExpect(status().isOk())
                .andDo(document("LoginRegisterInquiry"));
        }

        @DisplayName("연락처가 없는 1:1 문의 등록")
        @Test
        void noContact() throws Exception {
            mockMvc.perform(TestSnippet.post(url, objectMapper, notContactBoardDto))
                .andExpect(status().isBadRequest())
                .andDo(document("NoContactInquiry"));
        }

        @DisplayName("잘못된 토큰 - 1:1 문의 등록")
        @Test
        void wrongToken() throws Exception {
            mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, securedBoardDto))
                .andExpect(status().isBadRequest())
                .andDo(document("WrongTokenSaveInquiry"));
        }
    }

    @Nested
    @DisplayName("/inquiries/문의 전체 조회 테스트")
    class GetInquiries {
        private final String url = URL_PREFIX;
        private Board board;
        private Board board1;
        private List<Board> findBoard;

        @BeforeEach
        void boot() {
            board = Board.board("testContent", "testContact");
            board1 = Board.board("testContent1", "testContact1");
            boardRepository.save(board);
            boardRepository.save(board1);
            findBoard = boardRepository.findAll();
        }

        @DisplayName("정상적인 문의 전체 조회")
        @Test
        void fine() throws Exception {
            mockMvc.perform(TestSnippet.secureGet(url, adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value(findBoard.get(0).getContent()))
                .andExpect(jsonPath("$.[0].contact").value(findBoard.get(0).getContact()))
                .andExpect(jsonPath("$.[1].content").value(findBoard.get(1).getContent()))
                .andExpect(jsonPath("$.[1].contact").value(findBoard.get(1).getContact()))
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
            Board board = Board.board("testContent", "testContact");
            boardRepository.save(board);
            mockMvc.perform(TestSnippet.secured(get(url, board.getId()), adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(board.getContent()))
                .andExpect(jsonPath("$.contact").value(board.getContact()))
                .andDo(document("GetOneInquiry",
                    pathParameters(
                        parameterWithName("queryId").description("조회할 문의의 id")
                    )));
        }

        @DisplayName("잘못된 문의 선택 조회")
        @Test
        void wrongselect() throws Exception {
            mockMvc.perform(TestSnippet.secured(get(url, 1000), adminToken.getAccessToken()))
                .andExpect(status().isNotFound())
                .andDo(document("CannotFindInquiry"));
        }

        @DisplayName("잘못된 토큰 - 문의 선택 조회")
        @Test
        void wrongToken() throws Exception {
            Board board = Board.board("testContent", "testContact");
            boardRepository.save(board);
            mockMvc.perform(TestSnippet.secured(get(url, board.getId()), "testWrongToken"))
                .andExpect(status().isBadRequest())
                .andDo(document("WrongTokenGetInquiry"));
        }

    }
}