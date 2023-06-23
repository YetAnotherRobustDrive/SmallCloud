package org.mint.smallcloud.group.dto;


import com.fasterxml.jackson.annotation.JsonCreator;

public class GroupRequestDto {
    private final String groupName; // group name
    private final String parentName; // parent group id -> nullable

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GroupRequestDto(String groupName, String parentName) {
        this.groupName = groupName;
        this.parentName = parentName;
    }

    public static GroupRequestDtoBuilder builder() {
        return new GroupRequestDtoBuilder();
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getParentName() {
        return this.parentName;
    }

    public static class GroupRequestDtoBuilder {
        private String groupName;
        private String parentName;

        GroupRequestDtoBuilder() {
        }

        public GroupRequestDtoBuilder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public GroupRequestDtoBuilder parentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        public GroupRequestDto build() {
            return new GroupRequestDto(this.groupName, this.parentName);
        }

        public String toString() {
            return "GroupRequestDto.GroupRequestDtoBuilder(groupName=" + this.groupName + ", parentName=" + this.parentName + ")";
        }
    }
}
