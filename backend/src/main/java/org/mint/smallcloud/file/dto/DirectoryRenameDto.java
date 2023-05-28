package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class DirectoryRenameDto {
    private final String name;
}
