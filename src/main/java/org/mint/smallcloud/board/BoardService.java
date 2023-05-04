package org.mint.smallcloud.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final  BoardRepository boardRepository;

    public void getBoard() {
        Board board = new Board();
        BoardDto.builder()
                .contact(board.getContact())
                .content(board.getContent())
                .build();
        return boardRepository.findAll(board);
    }


}

