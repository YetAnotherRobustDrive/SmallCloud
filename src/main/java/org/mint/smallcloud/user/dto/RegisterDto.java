package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

@AllArgsConstructor
@Builder
@Getter
public class RegisterDto {

    @NicknameValidation
    private final String name;

    @UserNameValidation
    private final String id;

    @PasswordValidation
    private final String password;
}
