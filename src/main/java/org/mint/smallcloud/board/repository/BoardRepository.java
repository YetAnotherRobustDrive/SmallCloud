package org.mint.smallcloud.board.repository;

import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    List<Board> findByBoardType(BoardType type);

//    List<Board> findTop2ByBoardTypeOrderByCreatedDateDesc (BoardType type);
}
