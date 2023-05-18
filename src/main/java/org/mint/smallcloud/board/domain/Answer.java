package org.mint.smallcloud.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ANSWER")
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

    @OneToOne(mappedBy = "answer")
    @Column(name = "QUESTION_ID")
    private Question question;

    protected Answer(String title, String content, Question question){
        this.title = title;
        this.content = content;
        this.question = question;
        this.createdDate = LocalDateTime.now();
    }

    public static Answer answer(String title, String content, Question question) {
        return new Answer(title, content, question);
    }
}
