package org.mint.smallcloud.label.dto;

import org.mint.smallcloud.file.dto.DirectoryDto;
import org.mint.smallcloud.file.dto.FileDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class LabelFilesDto {
    @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.")
    @NotBlank
    private final String name;
    private final List<FileDto> files;
    private final List<DirectoryDto> folders;

    public LabelFilesDto(@Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name, List<FileDto> files, List<DirectoryDto> folders) {
        this.name = name;
        this.files = files;
        this.folders = folders;
    }

    public static LabelFilesDtoBuilder builder() {
        return new LabelFilesDtoBuilder();
    }

    public @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String getName() {
        return this.name;
    }

    public List<FileDto> getFiles() {
        return this.files;
    }

    public List<DirectoryDto> getFolders() {
        return this.folders;
    }

    public static class LabelFilesDtoBuilder {
        private @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name;
        private List<FileDto> files;
        private List<DirectoryDto> folders;

        LabelFilesDtoBuilder() {
        }

        public LabelFilesDtoBuilder name(@Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name) {
            this.name = name;
            return this;
        }

        public LabelFilesDtoBuilder files(List<FileDto> files) {
            this.files = files;
            return this;
        }

        public LabelFilesDtoBuilder folders(List<DirectoryDto> folders) {
            this.folders = folders;
            return this;
        }

        public LabelFilesDto build() {
            return new LabelFilesDto(this.name, this.files, this.folders);
        }

        public String toString() {
            return "LabelFilesDto.LabelFilesDtoBuilder(name=" + this.name + ", files=" + this.files + ", folders=" + this.folders + ")";
        }
    }
}
