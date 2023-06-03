package org.mint.smallcloud.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@Builder
public class DataNodeLabelDto {
    @NotBlank
    private final Long id;
    @NotNull
    private final String name;
    private final Long parentFolderId;
    private final LocalDateTime createdDate;
}
