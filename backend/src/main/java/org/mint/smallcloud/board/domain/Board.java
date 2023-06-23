package org.mint.smallcloud.board.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "BOARDS")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT", length = 5000)
    private String content;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "BOARD_TYPE")
    private BoardType boardType;

    /**
     * boardType == question,
     * writer != null -> 1:1문의
     */
//    @ManyToOne(targetEntity = Member.class)
//    @JoinColumn(name = "id")
//    private Member writer;
    protected Board(String title, String content, BoardType boardType) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = LocalDateTime.now();
    }

    protected Board() {
    }

    public static Board board(String title, String content, BoardType boardType) {
        return new Board(title, content, boardType);
    }


    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public BoardType getBoardType() {
        return this.boardType;
    }
}
