package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.BoardService;
import org.mint.smallcloud.board.dto.BoardDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/inquiries")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public ResponseEntity getInquiries() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{queryId}")
    public ResponseEntity getInquiry(@PathVariable("queryId") Long boardId) {
        return new ResponseEntity(HttpStatus.OK);
    }

}
