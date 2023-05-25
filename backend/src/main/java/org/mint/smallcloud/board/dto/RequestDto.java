package org.mint.smallcloud.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class RequestDto {

    @Size(min = 1, max = 500, message = "답변 내용은 500자 이하로 작성해 주세요.")
    @NotBlank(message = "답변 내용은 필수로 들어가야 합니다.")
    private final String content;

    @NotNull(message = "질문 테이블의 정보가 들어가야 합니다.")
    private final Long questionId;
}

