package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.Board;
import org.mint.smallcloud.board.BoardService;
import org.mint.smallcloud.board.dto.BoardDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/inquiries")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public List<Board> getInquiries() {
        return boardService.findAll();
    }

    @GetMapping("/{queryId}")
    public BoardDto getInquiry(@PathVariable("queryId") Long boardId) {
        return boardService.findById(boardId);
    }

    @PostMapping("/")
    public Long save(@RequestBody BoardDto boardDto) {
        return boardService.save(boardDto);
    }

}
