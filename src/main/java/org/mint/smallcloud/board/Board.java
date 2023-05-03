package org.mint.smallcloud.board;

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

    @Column(name = "CREATE_DATE")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "")
    private String Announcement;

    @OneToOne(mappedBy = "")
    private String Answer;

    @OneToOne(mappedBy = "")
    private String FAQ;

    @OneToOne(mappedBy = "")
    private String LoginQuestion;

    @OneToOne(mappedBy = "")
    private String Question;

    public Long getId() { return  id; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedDate() { return createdDate; }

    public void setTitle(String title) {}
}
