package com.yitkeji.songshushenghuo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RegValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReferralCode {

    String value() default "^\\w{8}$";

    String message() default "邀请码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
