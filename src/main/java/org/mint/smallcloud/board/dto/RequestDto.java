package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.board.domain.Board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class RequestDto {

    private  final String title;

    @Size(min = 1, max = 500, message = "문의 내용은 500자 이하로 작성해 주세요.")
    @NotBlank(message = "문의 내용은 필수로 들어가야 합니다.")
    private final String content;

    @NotBlank(message = "질문이 있는 보드의 정보가 들어가야 합니다.")
    private final Board board;
}

