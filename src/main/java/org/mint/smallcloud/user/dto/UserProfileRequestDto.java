package org.mint.smallcloud.user.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.file.domain.FileLocation;

@AllArgsConstructor
@Builder
@Getter
public class UserProfileRequestDto {
    private String username;
    private String nickname;
    @JsonUnwrapped
    private FileLocation profileImageLocation;
    private String groupName;
}
