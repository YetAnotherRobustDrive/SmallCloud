package org.mint.smallcloud.board;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOARDS")
@NoArgsConstructor
public class Board {

    @Id
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "boardId")
    private String announcement;

    @OneToOne(mappedBy = "boardId")
    private String answer;

    @OneToOne(mappedBy = "boardId")
    private String faq;

    @OneToOne(mappedBy = "boardId")
    private String loginQuestion;

    @OneToOne(mappedBy = "boardId")
    private String question;

    @OneToOne(mappedBy = "boardId")
    private String privacy;

    @OneToOne(mappedBy = "boardId")
    private String terms;

    public Long getId() { return  id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getContact() { return contact; }
    public LocalDateTime getCreatedDate() { return createdDate; }

    public void setTitle(String title) {}
    public void setContent(String content) {}
    public void setContact(String contact) {}

    @Builder
    public Board(String title, String content, String contact, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.createdDate = createdDate;
    }
}
