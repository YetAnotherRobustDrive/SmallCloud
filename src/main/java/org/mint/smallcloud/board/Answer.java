package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ANSWERS")
@NoArgsConstructor
public class Answer {

    @Id
    @Column(name = "ANSWER_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}
}
