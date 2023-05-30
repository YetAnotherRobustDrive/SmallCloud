package org.mint.smallcloud.group.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class GroupTreeDto {
    private final String name;
    private final List<GroupTreeDto> subGroups;
}
