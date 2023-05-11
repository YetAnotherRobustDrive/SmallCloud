package org.mint.smallcloud.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private final String contact;
    private final String content;
}
