package org.mint.smallcloud.board.serivce;

import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardThrowerService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BoardThrowerService.class);
    private final QuestionRepository questionRepository;
    private final BoardRepository boardRepository;

    public BoardThrowerService(QuestionRepository questionRepository, BoardRepository boardRepository) {
        this.questionRepository = questionRepository;
        this.boardRepository = boardRepository;
    }

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_INQUIRY));
    }

    public Board findBoardCreatedDate(BoardType boardType, int createdDate) {
        List<Board> BoardList = boardRepository.findTop2ByBoardTypeOrderByCreatedDateDesc(boardType);
        Board board;
        try {
            board = BoardList.get(createdDate);
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(ExceptionStatus.NOT_FOUND_INQUIRY);
        }
        return board;
    }
}
