package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
@NoArgsConstructor
public class Announcement {

    @Id
    @Column(name = "ANNOUNCEMENT_ID")
    private Long id;

    @OneToOne(mappedBy = "id")
    private Board boardId;
}
