package org.mint.smallcloud.board.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "BOARDS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @NotNull
    @Column(name = "CONTENT")
    private String content;

    @NotNull
    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id")
    private BoardType boardType;

    /**
     * boardType == question,
     * writer != null -> 1:1문의
     */
//    @ManyToOne
//    @JoinColumn(name = "board")
//    private Member writer;

    private Board(String content, String contact){
        this.content = content;
        this.contact = contact;
        this.createdDate = LocalDateTime.now();
    }

    public static Board of(String content, String contact) {
        return new Board(content, contact);
    }

}
