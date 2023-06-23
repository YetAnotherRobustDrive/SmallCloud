package org.mint.smallcloud.board.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ANSWER")
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANSWER_ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "answer")
    private Question question;


    protected Answer(String content, Question question) {
        this.content = content;
        this.createdDate = LocalDateTime.now();
        setQuestion(question);
    }

    protected Answer() {
    }

    public static Answer answer(String content, Question question) {
        return new Answer(content, question);
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

    public Long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Question getQuestion() {
        return this.question;
    }
}
