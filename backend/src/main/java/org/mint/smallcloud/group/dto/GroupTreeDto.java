package org.mint.smallcloud.group.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class GroupTreeDto {
    private final String name;
    private final List<GroupTreeDto> subGroups;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GroupTreeDto(String name, List<GroupTreeDto> subGroups) {
        this.name = name;
        this.subGroups = subGroups;
    }

    public static GroupTreeDtoBuilder builder() {
        return new GroupTreeDtoBuilder();
    }

    public String getName() {
        return this.name;
    }

    public List<GroupTreeDto> getSubGroups() {
        return this.subGroups;
    }

    public static class GroupTreeDtoBuilder {
        private String name;
        private List<GroupTreeDto> subGroups;

        GroupTreeDtoBuilder() {
        }

        public GroupTreeDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public GroupTreeDtoBuilder subGroups(List<GroupTreeDto> subGroups) {
            this.subGroups = subGroups;
            return this;
        }

        public GroupTreeDto build() {
            return new GroupTreeDto(this.name, this.subGroups);
        }

        public String toString() {
            return "GroupTreeDto.GroupTreeDtoBuilder(name=" + this.name + ", subGroups=" + this.subGroups + ")";
        }
    }
}
