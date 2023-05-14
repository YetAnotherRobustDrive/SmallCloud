package org.mint.smallcloud.validation;

import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(min = 1, max = 15, message = "아이디는 15자 이하로 작성해 주세요")
@NotBlank(message = "아이디는 필수로 들어가야 합니다.")
@Pattern(regexp = "^[a-zA-Z0-9]+", message = "아이디는 영어나 숫자만 가능합니다.")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameValidation {
    String message() default "아이디가 올바르지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}