package com.yitkeji.songshushenghuo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RegValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmsCode {

    String value() default "^\\w{6}$";

    String message() default "校验码错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
