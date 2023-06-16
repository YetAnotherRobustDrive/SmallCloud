package org.mint.smallcloud.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
public class AdminConfigDto {
    private final String code;
    private final String value;
}
