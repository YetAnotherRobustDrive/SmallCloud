package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ANNOUNCEMENTS")
@NoArgsConstructor
public class Announcement {

    @Id
    @Column(name = "ANNOUNCEMENT_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}
}
