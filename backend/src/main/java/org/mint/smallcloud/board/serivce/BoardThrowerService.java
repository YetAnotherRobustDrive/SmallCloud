package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardThrowerService {
    private final QuestionRepository questionRepository;
    private final BoardRepository boardRepository;

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
