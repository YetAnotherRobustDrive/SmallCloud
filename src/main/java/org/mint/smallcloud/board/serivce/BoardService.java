package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.dto.BoardCommonDto;
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

    public void save(BoardDto boardDto) {
        Board board = Board.board(
                boardDto.getContent(),
                boardDto.getContact());
        boardRepository.save(board);
    }

    public void saveCommon(BoardCommonDto boardCommonDto) {
        Board boardCommon = Board.boardCommon(
                boardCommonDto.getContent(),
                boardCommonDto.getContact(),
                boardCommonDto.getWriter());
        boardRepository.save(boardCommon);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public BoardDto findById(Long boardId) throws Exception {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception("특정 문의를 찾을 수 없습니다."));
        return BoardDto.builder()
                .content(board.getContent())
                .contact(board.getContact())
                .build();
    }


}

// 1:1 문의는 유저정보 필요
// terms 는 날짜순으로 정렬해서 최신 2개 필요
