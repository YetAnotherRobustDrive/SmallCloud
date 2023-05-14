package org.mint.smallcloud.user.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.file.domain.FileLocation;
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
    @JsonUnwrapped
    private FileLocation profileImageLocation;
    private String groupName;
}
