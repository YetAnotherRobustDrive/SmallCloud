package org.mint.smallcloud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

@AllArgsConstructor
@Builder
@Getter
public class LoginDto {
    @UserNameValidation
    private final String id;

    @PasswordValidation
    private final String password;
}
