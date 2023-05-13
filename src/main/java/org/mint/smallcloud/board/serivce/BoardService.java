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

    private final  BoardRepository boardRepository;
    private final BoardThrowerService boardThrowerService;

    public void save(BoardDto boardDto) {
        Board board = Board.board(
                boardDto.getContent(),
                boardDto.getContact(),
                boardDto.getWriter());
        boardRepository.save(board);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public BoardDto findById(Long boardId) throws Exception {

        Board board = boardThrowerService.findById(boardId);
        return BoardDto.builder()
                .content(board.getContent())
                .contact(board.getContact())
                .writer(board.getWriter())
                .build();
    }


}

// 1:1 문의는 유저정보 필요
// terms 는 날짜순으로 정렬해서 최신 2개 필요
