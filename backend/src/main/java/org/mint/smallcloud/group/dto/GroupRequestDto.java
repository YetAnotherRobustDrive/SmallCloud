package org.mint.smallcloud.group.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class GroupRequestDto {
    private final String groupName; // group name
    private final Long parentName; // parent group id -> nullable
    private final Integer y; // y position
    private final Integer x; // x position
}
