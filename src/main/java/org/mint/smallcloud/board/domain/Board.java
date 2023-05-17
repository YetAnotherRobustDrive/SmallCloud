package org.mint.smallcloud.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "BOARDS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @OneToOne
    @JoinColumn(name = "id")
    private BoardType boardType;

    /**
     * boardType == question,
     * writer != null -> 1:1문의
     */
//    @ManyToOne(targetEntity = Member.class)
//    @JoinColumn(name = "id")
//    private Member writer;

    @Column(name = "WRITER")
    private String writer;

    protected Board(String title, String content){
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }

    protected Board(String title, String content, String contact){
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.createdDate = LocalDateTime.now();
    }

    protected Board(String title, String content, BoardType boardType){
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = LocalDateTime.now();
    }

    protected Board(String title, String content, String contact, String writer){
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.writer = writer;
        this.createdDate = LocalDateTime.now();
    }

    protected Board(String title, String content, String contact, String writer, BoardType boardType){
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.writer = writer;
        this.boardType = boardType;
        this.createdDate = LocalDateTime.now();
    }


    public static Board board(String title, String content) {
        return new Board(title, content);
    }

    public static Board board(String title, String content, String contact) {
        return new Board(title, content, contact);
    }

    public static Board board(String title, String content, BoardType boardType) {
        return new Board(title, content, boardType);
    }

    public static Board board(String title, String content, String contact, String writer) {
        return new Board(title, content, contact, writer);
    }

    public static Board board(String title, String content, String contact, String writer, BoardType boardType) {
        return new Board(title, content, contact, writer, boardType);
    }
}
