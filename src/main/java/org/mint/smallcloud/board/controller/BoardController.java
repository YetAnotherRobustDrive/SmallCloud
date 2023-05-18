package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.serivce.BoardService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/inquiries")
@Slf4j
@RequiredArgsConstructor
@Validated
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
    public ResponseEntity<?> save(HttpServletRequest request, @Valid @RequestBody BoardDto boardDto) {
        if (!Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(String::isBlank).orElse(true)
            && userDetailsProvider.getUserDetails().isEmpty()) {
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        }
        Optional<UserDetails> authentication = userDetailsProvider.getUserDetails();
        BoardDto boardDto1 = BoardDto.builder()
                .title(boardDto.getTitle())
            .content(boardDto.getContent())
            .contact(boardDto.getContact())
            .writer(authentication.map(UserDetails::getUsername).orElse(null))
            .build();
        boardService.save(boardDto1);
        return ResponseEntity.ok().build();
    }

    @Secured({Roles.S_ADMIN})
    @PostMapping("/answer")
    public ResponseDto<Boolean> registerAnswer(@Valid @RequestBody RequestDto requestDto) {
        boolean result = boardService.registerAnswer(requestDto);
        return ResponseDto.<Boolean>builder().result(result).build();
    }

    @Secured({Roles.S_ADMIN})
    @GetMapping("/questioned")
    public List<Board> getQuestioned( return BoardService.getQuestioned())
}
