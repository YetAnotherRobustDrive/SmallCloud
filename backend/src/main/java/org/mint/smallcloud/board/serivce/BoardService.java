package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public boolean saveBoard(BoardDto boardDto) {
        Board board = Board.board(
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getBoardType());
        boardRepository.save(board);
        return true;
    }
}
