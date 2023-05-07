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

    public Long save(BoardDto boardDto) {
        Board board = new Board();
        Board boardEntity = Board.builder()
                .title(board.getTitle())
                .content(boardDto.getContent())
                .contact(boardDto.getContact())
                .createdDate(board.getCreatedDate())
                .build();
        return boardRepository.save(boardEntity).getId();
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public BoardDto findById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow();
        return BoardDto.builder()
                .content(board.getContent())
                .contact(board.getContact())
                .build();
    }


}

// 1:1 문의는 유저정보 필요
// terms 는 날짜순으로 정렬해서 최신 2개 필요
