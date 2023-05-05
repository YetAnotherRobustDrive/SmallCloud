package org.mint.smallcloud.board.repository;

import org.mint.smallcloud.board.Board;
import org.mint.smallcloud.board.dto.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
