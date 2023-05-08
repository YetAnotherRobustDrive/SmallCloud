package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "FAQS")
@NoArgsConstructor
public class FAQ {

    @Id
    @Column(name = "FAQ_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}
}
