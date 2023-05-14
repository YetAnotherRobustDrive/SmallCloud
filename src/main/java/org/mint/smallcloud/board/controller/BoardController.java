package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.serivce.BoardService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/inquiries")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserDetailsProvider userDetailsProvider;

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
        Optional<UserDetails> authentication = userDetailsProvider.getUserDetails();
        authentication.get().getUsername();
        authentication.orElseGet()
        BoardDto boardDto1 = BoardDto.builder()
                .content(boardDto.getContent())
                .contact(boardDto.getContact())
                .writer()
                .build();
        boardService.save(boardDto1);
        return ResponseEntity.ok().build();
    }
}
