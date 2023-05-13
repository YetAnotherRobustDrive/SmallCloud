package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.dto.BoardCommonDto;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.serivce.BoardService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/inquiries")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Secured({Roles.S_ADMIN})
    @GetMapping
    public List<Board> getInquiries() {
        return boardService.findAll();
    }

    @Secured({Roles.S_ADMIN})
    @GetMapping("/{queryId}")
    public BoardDto getInquiry(@PathVariable("queryId") Long boardId) throws Exception {
        return boardService.findById(boardId);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody BoardDto boardDto) {
        boardService.save(boardDto);
        return ResponseEntity.ok().build();
    }

    /**
     * @Secured 를 어떻게 처리해야 할지 모르겠음
     * 1:1 문의는 role 이 붙어야 하는데,
     * 비로그인 문의는 role이 안붙으니까
     * 일단은 따로 만들었음
     */
    @Secured({Roles.S_COMMON})
    @PostMapping("/common")
    public ResponseEntity<?> saveCommon(@RequestBody BoardCommonDto boardCommonDto) {
        boardService.saveCommon(boardCommonDto);
        return ResponseEntity.ok().build();
    }

}
