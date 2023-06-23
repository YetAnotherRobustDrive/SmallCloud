package org.mint.smallcloud.file.dto;

import java.util.List;

public class LabelUpdateDto {
    private final Long fileId;
    private final List<String> labels;

    public LabelUpdateDto(Long fileId, List<String> labels) {
        this.fileId = fileId;
        this.labels = labels;
    }

    public static LabelUpdateDtoBuilder builder() {
        return new LabelUpdateDtoBuilder();
    }

    public Long getFileId() {
        return this.fileId;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public static class LabelUpdateDtoBuilder {
        private Long fileId;
        private List<String> labels;

        LabelUpdateDtoBuilder() {
        }

        public LabelUpdateDtoBuilder fileId(Long fileId) {
            this.fileId = fileId;
            return this;
        }

        public LabelUpdateDtoBuilder labels(List<String> labels) {
            this.labels = labels;
            return this;
        }

        public LabelUpdateDto build() {
            return new LabelUpdateDto(this.fileId, this.labels);
        }

        public String toString() {
            return "LabelUpdateDto.LabelUpdateDtoBuilder(fileId=" + this.fileId + ", labels=" + this.labels + ")";
        }
    }
}
