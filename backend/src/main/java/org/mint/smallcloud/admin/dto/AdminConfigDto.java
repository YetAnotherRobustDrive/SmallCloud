package org.mint.smallcloud.admin.dto;

public class AdminConfigDto {
    private final String code;
    private final String value;

    public AdminConfigDto(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static AdminConfigDtoBuilder builder() {
        return new AdminConfigDtoBuilder();
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static class AdminConfigDtoBuilder {
        private String code;
        private String value;

        AdminConfigDtoBuilder() {
        }

        public AdminConfigDtoBuilder code(String code) {
            this.code = code;
            return this;
        }

        public AdminConfigDtoBuilder value(String value) {
            this.value = value;
            return this;
        }

        public AdminConfigDto build() {
            return new AdminConfigDto(this.code, this.value);
        }

        public String toString() {
            return "AdminConfigDto.AdminConfigDtoBuilder(code=" + this.code + ", value=" + this.value + ")";
        }
    }
}
