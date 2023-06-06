package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.PasswordValidation;
import org.mint.smallcloud.validation.UserNameValidation;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class RegisterFromAdminDto {
    @NicknameValidation
    @NotBlank(message = BlankMessages.NICKNAME)
    private final String name;

    @UserNameValidation
    @NotBlank(message = BlankMessages.USER_NAME)
    private final String id;

    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;

    private final LocalDateTime expiredDate;
}
