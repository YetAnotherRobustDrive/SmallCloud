package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DirectoryRenameDto {
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DirectoryRenameDto(String name) {
        this.name = name;
    }

    public static DirectoryRenameDtoBuilder builder() {
        return new DirectoryRenameDtoBuilder();
    }

    public String getName() {
        return this.name;
    }

    public static class DirectoryRenameDtoBuilder {
        private String name;

        DirectoryRenameDtoBuilder() {
        }

        public DirectoryRenameDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DirectoryRenameDto build() {
            return new DirectoryRenameDto(this.name);
        }

        public String toString() {
            return "DirectoryRenameDto.DirectoryRenameDtoBuilder(name=" + this.name + ")";
        }
    }
}
