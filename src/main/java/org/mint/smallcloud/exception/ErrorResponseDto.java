package org.mint.smallcloud.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
    private final String error;
}
