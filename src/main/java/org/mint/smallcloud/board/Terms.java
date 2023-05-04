package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TERMS")
@NoArgsConstructor
public class Terms {

    @Id
    @Column(name = "TERMS_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}

}
