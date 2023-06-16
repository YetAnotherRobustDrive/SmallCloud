package org.mint.smallcloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class PasswordChangedDateDto {
    private final LocalDateTime changedPasswordDate;
}
