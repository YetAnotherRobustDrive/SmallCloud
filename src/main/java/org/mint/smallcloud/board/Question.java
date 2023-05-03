package org.mint.smallcloud.board;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES")
@NoArgsConstructor
public class Question {

    @Id
    @Column(name = "QUESTION_ID")
    private Long id;
}
