package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.domain.BoardType;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.dto.BoardDto;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.repository.AnswerRepository;
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
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getContact(),
                boardDto.getWriter(),
                BoardType.question);
        boardRepository.save(board);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public BoardDto findById(Long boardId) throws Exception {

        Board board = boardThrowerService.findById(boardId);
        return BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .contact(board.getContact())
                .writer(board.getWriter())
                .build();
    }



    public List<Board> getQuestioned() {
        return boardRepository.find
    }
}
