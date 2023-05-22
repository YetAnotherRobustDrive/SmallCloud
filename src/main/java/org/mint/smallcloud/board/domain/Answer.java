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
    private Question question;


    protected Answer(String title, String content, Question question) {
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        setQuestion(question);
    }

    public static Answer answer(String title, String content, Question question) {
        return new Answer(title, content, question);
    }

    public void setQuestion(Question question) {
        if (question == null || this.question == question)
            return;
        this.question = question;
        question.setAnswer(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Answer) {
            Answer answer = (Answer) obj;
            return this.id.equals(answer.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
