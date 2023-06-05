package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.NicknameValidation;
import org.mint.smallcloud.validation.UserNameValidation;

@AllArgsConstructor
@Builder
@Getter
public class UserProfileRequestDto {
    @UserNameValidation
    private String username;
    @NicknameValidation
    private String nickname;
}
