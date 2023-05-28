package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class DirectoryCreateDto {
    private final String name;
}
