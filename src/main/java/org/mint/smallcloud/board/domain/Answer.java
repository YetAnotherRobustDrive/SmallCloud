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
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "id")
    @Column(name = "BOARD_ID")
    private Board boardId;

    protected Answer(String title, String content, Board boardId){
        this.title = title;
        this.content = content;
        this.boardId = boardId;
        this.createdDate = LocalDateTime.now();
    }

    public static Answer answer(String title, String content, Board boardId) {
        return new Answer(title, content, boardId);
    }
}
