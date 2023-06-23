package org.mint.smallcloud.label.dto;

import org.mint.smallcloud.user.dto.UserLabelDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LabelDto {
    @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.")
    @NotBlank
    private final String name;
    private final UserLabelDto owner;

    public LabelDto(@Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name, UserLabelDto owner) {
        this.name = name;
        this.owner = owner;
    }

    public static LabelDtoBuilder builder() {
        return new LabelDtoBuilder();
    }

    public @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String getName() {
        return this.name;
    }

    public UserLabelDto getOwner() {
        return this.owner;
    }

    public static class LabelDtoBuilder {
        private @Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name;
        private UserLabelDto owner;

        LabelDtoBuilder() {
        }

        public LabelDtoBuilder name(@Size(min = 1, max = 10, message = "라벨 이름은 10자 이하로 작성해주세요.") @NotBlank String name) {
            this.name = name;
            return this;
        }

        public LabelDtoBuilder owner(UserLabelDto owner) {
            this.owner = owner;
            return this;
        }

        public LabelDto build() {
            return new LabelDto(this.name, this.owner);
        }

        public String toString() {
            return "LabelDto.LabelDtoBuilder(name=" + this.name + ", owner=" + this.owner + ")";
        }
    }
}
