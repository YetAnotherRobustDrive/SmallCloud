package org.mint.smallcloud.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "QUESTION")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "writer")
    private String writer;

    @OneToOne
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

    protected Question(String title, String content){
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }

    protected Question(String title, String content, Long id){
        this.title = title;
        this.content = content;
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    protected Question(String title, String content, String contact){
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.createdDate = LocalDateTime.now();
    }


    protected Question(String title, String content, String contact, String writer){
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.writer = writer;
        this.createdDate = LocalDateTime.now();
    }


    public static Question question(String title, String content) {
        return new Question(title, content);
    }

    public static Question question(String title, String content, Long id) {
        return new Question(title, content, id);
    }

    public static Question question(String title, String content, String contact) {
        return new Question(title, content, contact);
    }

    public static Question question(String title, String content, String contact, String writer) {
        return new Question(title, content, contact, writer);
    }

}
