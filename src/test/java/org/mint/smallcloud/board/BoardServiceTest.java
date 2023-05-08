package org.mint.smallcloud.board;

import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mint.smallcloud.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        boardService = new BoardService(boardRepository);
    }

    @Test
    void save() {
        Board board = new Board();
        board.setTitle("testTitle");
        board.setContact("testEmail@gmail.com");
        board.setContent("testContent");
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4,23,12,34,56);


    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }
}