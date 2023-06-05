package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
public class PasswordUpdateRequestDto {
    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String newPassword;
}
