package com.yitkeji.songshushenghuo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RegValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserName {
    String value() default "\\d{11}";

    String message() default "用户名格式不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
