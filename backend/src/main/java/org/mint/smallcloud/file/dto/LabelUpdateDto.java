package org.mint.smallcloud.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class LabelUpdateDto {
    private final Long fileId;
    private final List<String> labels;
}
