package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.board.domain.BoardType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private final String title;

    private final String content;

    private final BoardType boardType;
}

