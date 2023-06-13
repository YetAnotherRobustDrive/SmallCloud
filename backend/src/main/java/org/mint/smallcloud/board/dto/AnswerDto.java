package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AnswerDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdDate;
}
