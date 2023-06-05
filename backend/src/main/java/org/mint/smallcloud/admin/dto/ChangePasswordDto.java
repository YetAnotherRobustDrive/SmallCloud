package org.mint.smallcloud.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.BlankMessages;
import org.mint.smallcloud.validation.PasswordValidation;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor(onConstructor_ = { @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) })
public class ChangePasswordDto {
    @PasswordValidation
    @NotBlank(message = BlankMessages.PASSWORD)
    private final String password;
}
