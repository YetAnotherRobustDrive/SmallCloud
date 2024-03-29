package org.mint.smallcloud.label.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.user.dto.UserLabelDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class LabelDto {
    @Size(min=1, max=10, message = "라벨 이름은 10자 이하로 작성해주세요.")
    @NotBlank
    private final String name;
    private final UserLabelDto owner;
}
