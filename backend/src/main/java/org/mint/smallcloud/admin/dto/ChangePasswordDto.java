package org.mint.smallcloud.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.validation.PasswordValidation;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
public class ChangePasswordDto {
    @PasswordValidation
    @NotBlank
    private final String password;
}
