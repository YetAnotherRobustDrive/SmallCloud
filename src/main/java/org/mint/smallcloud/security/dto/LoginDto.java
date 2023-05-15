package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
public class LoginDto {
    @UserNameValidation
    @NotBlank(message = BlankMessages.USER_NAME)
    private final String id;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;
}
