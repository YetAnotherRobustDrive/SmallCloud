package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.board.domain.BoardType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private final String title;

    @Size(min = 1)
    @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.")
    private final String content;

    @NotNull(message = "보드 타입은 생략될 수 없습니다.")
    private final BoardType boardType;
}

