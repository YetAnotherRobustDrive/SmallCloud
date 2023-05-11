package org.mint.smallcloud.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.board.BoardDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("/inquiries/문의 등록 테스트")
    public void save() throws Exception {
        final String url = URL_PREFIX;
        RequestFieldsSnippet payload = requestFields(
                fieldWithPath("content").description("사용자 질문의 내용을 담고 있습니다."),
                fieldWithPath("contact").description("사용자의 연락처를 담고 있습니다."));
        BoardDto boardDto =
                BoardDto.builder()
                        .content("testContent")
                        .contact("testContact")
                        .build();

        this.mockMvc.perform(TestSnippet.post(url, objectMapper, boardDto))
                .andExpect(status().isOk())
                .andDo(document("Save",payload));
    }

    @Test
    @DisplayName("/inquiries/문의 전체 조회 테스트")
    public void getInquiries() throws Exception {
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

        Board board = Board.of("testContent","testContact");
        Board board1 = Board.of("testContent1","testContact1");

        boardRepository.save(board);
        boardRepository.save(board1);
        boardRepository.findAll();

        this.mockMvc.perform(TestSnippet.secureGet(url,adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value("testContent"))
                .andExpect(jsonPath("$.[0].contact").value("testContact"))
                .andExpect(jsonPath("$.[1].content").value("testContent1"))
                .andExpect(jsonPath("$.[1].contact").value("testContact1"))
                .andDo(document("GetAllInquiries"));
    }
}