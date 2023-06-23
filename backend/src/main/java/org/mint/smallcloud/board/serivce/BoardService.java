package org.mint.smallcloud.board.serivce;

import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.mapper.BoardMapper;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.mint.smallcloud.notification.event.NoticeAllEventAfterCommit;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BoardService.class);
    private final BoardRepository boardRepository;
    private final BoardThrowerService boardThrowerService;
    private final BoardMapper boardMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BoardService(BoardRepository boardRepository, BoardThrowerService boardThrowerService, BoardMapper boardMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.boardRepository = boardRepository;
        this.boardThrowerService = boardThrowerService;
        this.boardMapper = boardMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean saveBoard(BoardDto boardDto) {
        Board board = Board.board(
            boardDto.getTitle(),
            boardDto.getContent(),
            boardDto.getBoardType());
        boardRepository.save(board);
        applicationEventPublisher.publishEvent(
            NoticeAllEventAfterCommit
                .builder()
                .content(String.format("%s이(가) 업데이트 되었습니다.", board.getBoardType().getMessage()))
                .build());
        return true;
    }

    public List<BoardDto> findBoard(BoardType boardType) {
        List<Board> board = boardRepository.findByBoardType(boardType);
        return board.stream().map(boardMapper::toBoardDto).collect(Collectors.toList());
    }

    public BoardDto findBoardCreatedDate(BoardType boardType, int createdDate) {
        Board board = boardThrowerService.findBoardCreatedDate(boardType, createdDate);

        return BoardDto.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .boardType(board.getBoardType())
            .build();
    }
}
