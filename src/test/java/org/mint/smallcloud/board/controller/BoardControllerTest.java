package org.mint.smallcloud.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.repository.BoardRepository;
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
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    private MockMvc mockMvc;
    private final String URL_PREFIX = "/inquiries";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("/inquiries/문의 등록 테스트")
    public void save() throws Exception {
        final String url = URL_PREFIX;
        Member member = Member.createCommon("testMemberName", "testPw", "testNickname");
        memberRepository.save(member);
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .disabled(member.isLocked())
                .roles(member.getRole())
                .build();
        JwtTokenDto memberToken = jwtTokenProvider.generateTokenDto(userDetailsDto);

        RequestFieldsSnippet payload = requestFields(
                fieldWithPath("content").description("사용자 질문의 내용을 담고 있습니다."),
                fieldWithPath("contact").description("사용자의 연락처를 담고 있습니다."),
                fieldWithPath("writer").description("글의 작성자를 담고 있습니다."));
        BoardDto securedBoardDto =
                BoardDto.builder()
                        .content("testContent")
                        .contact("testContact")
                        .writer("testWriter")
                        .build();

        BoardDto boardDto =
                BoardDto.builder()
                        .content("testContent1")
                        .contact("testContact1")
                        .build();

        BoardDto notContactBoardDto =
                BoardDto.builder()
                        .contact("testContact1")
                        .build();

        // 정상적 1:1 문의 등록
        this.mockMvc.perform(TestSnippet.securePost(url, memberToken.getAccessToken(), objectMapper, securedBoardDto))
                .andExpect(status().isOk())
                .andDo(document("OneToOneRegisterInquiry",payload));

        // 정상적 로그인 문의 등록
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, boardDto))
                .andExpect(status().isOk())
                .andDo(document("LoginRegisterInquiry",payload));

        // 연락처가 없음 - 로그인 문의 등록
        this.mockMvc.perform(TestSnippet.post(url, objectMapper, notContactBoardDto))
                .andExpect(status().isNoContent())
                .andDo(document("NoContactInquiry",payload));

        // 잘못된 토큰 - 1:1 문의 등록
        this.mockMvc.perform(TestSnippet.securePost(url, "testWrongToken", objectMapper, securedBoardDto))
                .andExpect(status().isBadRequest())
                .andDo(document("WrongTokenSaveInquiry",payload));
    }


    @Test
    @DisplayName("/inquiries/문의 전체 조회 테스트")
    public void getInquiries() throws Exception {
        final String url = URL_PREFIX;
        Member admin = Member.createAdmin("testAdminName", "testPw", "testNickname");
        memberRepository.save(admin);
        UserDetailsDto adminDto = UserDetailsDto.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.isLocked())
                .roles(admin.getRole())
                .build();
        JwtTokenDto adminToken = jwtTokenProvider.generateTokenDto(adminDto);

        Board board = Board.board("testContent","testContact");
        Board board1 = Board.board("testContent1","testContact1");

        boardRepository.save(board);
        boardRepository.save(board1);
        List<Board> findBoard = boardRepository.findAll();

        this.mockMvc.perform(TestSnippet.secureGet(url,adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value(findBoard.get(0).getContent()))
                .andExpect(jsonPath("$.[0].contact").value(findBoard.get(0).getContact()))
                .andExpect(jsonPath("$.[1].content").value(findBoard.get(1).getContent()))
                .andExpect(jsonPath("$.[1].contact").value(findBoard.get(1).getContact()))
                .andDo(document("GetAllInquiries"));

        this.mockMvc.perform(TestSnippet.secureGet(url, adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(document("CannotFindAllBoardContent"));
    }

    @Test
    @DisplayName("/inquiries/문의 선택 조회 테스트")
    public void getInquiry() throws Exception {
        final String url = URL_PREFIX;
        Member admin = Member.createAdmin("testUsername", "testPw", "testNickname");
        memberRepository.save(admin);
        UserDetailsDto adminDto = UserDetailsDto.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.isLocked())
                .roles(admin.getRole())
                .build();
        JwtTokenDto adminToken = jwtTokenProvider.generateTokenDto(adminDto);

        Board board = Board.board("testContent","testContact");
        Board board1 = Board.board("testContent1","testContact1");

        boardRepository.save(board);
        boardRepository.save(board1);
        Optional<Board> findBoard = boardRepository.findById(1L);

        String expectByContent = "$[%s].content";
        String expectByContact = "$[%s].contact";

        this.mockMvc.perform(TestSnippet.secureGet(url,adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(expectByContent, 0).value(findBoard.get().getContent()))
                .andExpect(jsonPath(expectByContact, 0).value(findBoard.get().getContact()))
                .andDo(document("GetInquiry"));

        this.mockMvc.perform(TestSnippet.secureGet(url,adminToken.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(expectByContent, 2).value(findBoard.get().getContent()))
                .andExpect(jsonPath(expectByContact, 2).value(findBoard.get().getContact()))
                .andDo(document("CannotFindInquiry"));

        this.mockMvc.perform(TestSnippet.secureGet(url, "testWrongToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(expectByContent, 0).value(findBoard.get().getContent()))
                .andExpect(jsonPath(expectByContact, 0).value(findBoard.get().getContact()))
                .andDo(document("WrongTokenGetInquiry"));
    }
}