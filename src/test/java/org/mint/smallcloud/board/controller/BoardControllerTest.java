package org.mint.smallcloud.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.board.Board;
import org.mint.smallcloud.board.BoardService;
import org.mint.smallcloud.board.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

//    @MockBean
//    private BoardService boardService;

//    @BeforeEach
//    public void setUp() {
//        mvc = MockMvcBuilders.standaloneSetup(new BoardController(boardService))
//                .addFilters(new CharacterEncodingFilter("UTF-8",true))
//                .build();
//    }

    @Test
    void getInquiries() {
    }

    @Test
    void getInquiry() {
    }

    @Test
    @DisplayName("문의 답변 테스트")
    void save() throws Exception {

        String title = "testTitle";
        String content = "testContent";
        String contact = "testContact";
        LocalDateTime createdDate = LocalDateTime.of(2023, 4,23,12,34,56);

        String body = mapper.writeValueAsString(
                BoardDto.builder()
                        .content(content)
                        .contact(contact)
                        .build()
        );

        mvc.perform(post("/inquiries/")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));


//        given(boardService.save(any())).willReturn(
//                Board.builder()
//                        .title("testTitle")
//                        .contact("testEmail")
//                        .content("testContent")
//                        .createdDate(LocalDateTime.of(2023, 4,23,12,34,56))
//                        .build());
//
//        final ResultActions actions =
//                mvc.perform(
//                        post("/")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .characterEncoding("UTF-8")
//                                .content(
//                                        "{"
//                                        + " \"title\" : \"testTitle\", "
//                                        + " \"contact\" : \"testEmail\", "
//                                        + " \"content\" : \"testContent\" "
//                                        + "}"));
//
//        actions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("title").value("testTitle"))
//                .andExpect(jsonPath("contact").value("testEmail"))
//                .andExpect(jsonPath("content").value("testContent"));
    }
}