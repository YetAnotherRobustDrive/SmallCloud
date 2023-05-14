package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Builder
@Getter
public class RegisterDto {

    @Size(min = 1, max = 15, message = "이름 15자 이하로 작성해 주세요")
    @NotBlank(message = "이름은 필수로 들어가야 합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]+", message = "이름은 영어나 숫자나 한글만 가능합니다.")
    private final String name;

    @Size(min = 1, max = 15, message = "아이디는 15자 이하로 작성해 주세요")
    @NotBlank(message = "아이디는 필수로 들어가야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+", message = "아이디는 영어나 숫자만 가능합니다.")
    private final String id;

    @Size(min = 1, max = 15, message = "비밀번호는 15이하로 작성해 주세요")
    @NotBlank(message = "비밀번호는 필수로 들어가야 합니다.")
    private final String password;
}
