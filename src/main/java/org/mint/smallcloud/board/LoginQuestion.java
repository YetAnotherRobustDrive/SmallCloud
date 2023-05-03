package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES")
@NoArgsConstructor
public class LoginQuestion {

    @Id
    @Column(name = "LOGINQUESTION_ID")
    private Long id;
}
