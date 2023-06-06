package org.mint.smallcloud.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.dto.QuestionDto;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.serivce.AnswerService;
import org.mint.smallcloud.board.serivce.BoardService;
import org.mint.smallcloud.board.serivce.QuestionService;
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

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final BoardService boardService;
    private final UserDetailsProvider userDetailsProvider;

    // 전체 question 가져오기
    @Secured({Roles.S_ADMIN})
    @GetMapping
    public List<QuestionDto> getInquiries() {
        return questionService.findAll();
    }

    // 특정 question 가져오기
    @Secured({Roles.S_ADMIN})
    @GetMapping("/{queryId}")
    public QuestionDto getInquiry(@PathVariable("queryId") Long questionId) throws Exception {
        return questionService.findById(questionId);
    }

    // 1:1 문의 등록 + 비로그인 문의 등록
    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest request, @Valid @RequestBody QuestionDto questionDto) {
        if (!Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(String::isBlank).orElse(true)
            && userDetailsProvider.getUserDetails().isEmpty()) {
            throw new ServiceException(ExceptionStatus.NOT_VALID_JWT_TOKEN);
        }
        Optional<UserDetails> authentication = userDetailsProvider.getUserDetails();
        QuestionDto questionDto1 = QuestionDto.builder()
                .title(questionDto.getTitle())
            .content(questionDto.getContent())
            .contact(questionDto.getContact())
            .writer(authentication.map(UserDetails::getUsername).orElse(null))
            .build();
        questionService.save(questionDto1);
        return ResponseEntity.ok().build();
    }

    // 문의 답변 등록
    @Secured({Roles.S_ADMIN})
    @PostMapping("/answer")
    public ResponseDto<Boolean> registerAnswer(@Valid @RequestBody RequestDto requestDto) {
        boolean result = answerService.registerAnswer(requestDto);
        return ResponseDto.<Boolean>builder().result(result).build();
    }

    // 문의 비답변 목록 가져오기
    @Secured({Roles.S_ADMIN})
    @GetMapping("/questioned")
    public List<QuestionDto> getQuestioned() {
        return questionService.findQuestioned();
    }

    // 내 문의 내역 리스트 가져오기
    @Secured({Roles.S_COMMON})
    @GetMapping("/myQuestions")
    public List<QuestionDto> getMyQuestions(@RequestParam("writer") String writer) {
        return questionService.findMyQuestions(writer);
    }

    // board 등록
    @Secured({Roles.S_ADMIN})
    @PostMapping("/board")
    public ResponseDto<Boolean> saveBoard(@Valid @RequestBody BoardDto boardDto) {
        boolean result = boardService.saveBoard(boardDto);
        return ResponseDto.<Boolean>builder().result(result).build();
    }

    // board 조회
    @Secured({Roles.S_ADMIN, Roles.S_COMMON})
    @GetMapping("/board")
    public List<BoardDto> getBoard(@RequestParam BoardType boardType) {
        return boardService.findBoard(boardType);
    }

    // board 최신, 이전 내용조회
    @Secured({Roles.S_ADMIN, Roles.S_COMMON})
    @GetMapping("/board/created")
    public BoardDto getBoardCreatedDate(@RequestParam BoardType boardType, @RequestParam int createdDate) {
           return boardService.findBoardCreatedDate(boardType, createdDate);
    }
}
