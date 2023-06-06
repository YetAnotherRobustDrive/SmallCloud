package org.mint.smallcloud.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationDto {
    @Size(min = 1, max = 50, message = "1자 이상 50자 이하로 입력해주세요.")
    @NotBlank
    String content;
    LocalDateTime localDateTime;
}
