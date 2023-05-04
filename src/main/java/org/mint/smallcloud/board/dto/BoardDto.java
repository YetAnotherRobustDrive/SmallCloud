package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private String contact;
    private String content;
}
