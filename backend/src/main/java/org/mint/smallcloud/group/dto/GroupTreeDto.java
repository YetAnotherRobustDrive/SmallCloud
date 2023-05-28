package org.mint.smallcloud.group.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class GroupTreeDto {
    private final Long parentName;
    private final Long groupName;
    private final Integer y;
    private final Integer x;
}
