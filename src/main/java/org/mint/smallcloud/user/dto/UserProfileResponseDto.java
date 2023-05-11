package org.mint.smallcloud.user.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.user.domain.Role;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class UserProfileResponseDto {
    private String username;
    private String nickname;
    private LocalDateTime joinedDate;
    private LocalDateTime changedPasswordDate;
    private boolean isLocked;
    @JsonUnwrapped
    private FileLocation profileImageLocation;
    private Role role;
    private String groupName;
}
