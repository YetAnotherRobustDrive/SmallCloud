package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.mapper.BoardMapper;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public boolean saveBoard(BoardDto boardDto) {
        Board board = Board.board(
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getBoardType());
        boardRepository.save(board);
        return true;
    }

    public List<BoardDto> findBoard(BoardType boardType) {
        List<Board> board = boardRepository.findByBoardType(boardType);
        return board.stream().map(boardMapper::toBoardDto).collect(Collectors.toList());
    }
}
