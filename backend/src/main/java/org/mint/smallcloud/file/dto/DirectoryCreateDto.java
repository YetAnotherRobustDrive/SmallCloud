package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DirectoryCreateDto {
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DirectoryCreateDto(String name) {
        this.name = name;
    }

    public static DirectoryCreateDtoBuilder builder() {
        return new DirectoryCreateDtoBuilder();
    }

    public String getName() {
        return this.name;
    }

    public static class DirectoryCreateDtoBuilder {
        private String name;

        DirectoryCreateDtoBuilder() {
        }

        public DirectoryCreateDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DirectoryCreateDto build() {
            return new DirectoryCreateDto(this.name);
        }

        public String toString() {
            return "DirectoryCreateDto.DirectoryCreateDtoBuilder(name=" + this.name + ")";
        }
    }
}
