package org.mint.smallcloud.board.repository;

import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWriter(String writer);

    List<Board> findByBoardType(BoardType type);

//    @Query("select board FROM Board board where board.boardType = :type order by board.id limit 2")
//    List<Board> findTop2ByBoardType(BoardType type);
}
