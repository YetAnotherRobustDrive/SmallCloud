package org.mint.smallcloud.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UsageDto {
    private final Long used;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UsageDto(Long used) {
        this.used = used;
    }

    public static UsageDtoBuilder builder() {
        return new UsageDtoBuilder();
    }

    public Long getUsed() {
        return this.used;
    }

    public static class UsageDtoBuilder {
        private Long used;

        UsageDtoBuilder() {
        }

        public UsageDtoBuilder used(Long used) {
            this.used = used;
            return this;
        }

        public UsageDto build() {
            return new UsageDto(this.used);
        }

        public String toString() {
            return "UsageDto.UsageDtoBuilder(used=" + this.used + ")";
        }
    }
}