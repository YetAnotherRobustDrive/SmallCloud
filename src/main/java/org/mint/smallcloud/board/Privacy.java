package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PRIVACY")
@NoArgsConstructor
public class Privacy {

    @Id
    @Column(name = "PRIVACY_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}
}
