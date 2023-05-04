package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;
import org.mint.smallcloud.security.user.User;

import javax.persistence.*;

@Entity
@Table(name = "QUESTIONS")
@NoArgsConstructor
public class Question {

    @Id
    @Column(name = "QUESTION_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() { return id; }
    public Board getBoardId() { return boardId; }

}
