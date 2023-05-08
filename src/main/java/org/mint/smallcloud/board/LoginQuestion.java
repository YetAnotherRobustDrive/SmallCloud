package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;
import org.mint.smallcloud.security.user.User;

import javax.persistence.*;

@Entity
@Table(name = "LOGINQUESTIONS")
@NoArgsConstructor
public class LoginQuestion {

    @Id
    @Column(name = "LOGINQUESTION_ID")
    private Long id;

    @OneToOne
    private Board boardId;

    @ManyToOne
    @JoinColumn(name = "WRITER")
    private User writer;

    public Long getId() {return id;}
    public Board getBoardId() {return boardId;}
    public User getWriter() { return writer; }
}
