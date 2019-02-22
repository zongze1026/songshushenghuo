package com.yitkeji.songshushenghuo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = RegValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreditCardBillDay {

    String value() default "^(1[0-9]|2[0-9]|3[0-1]|0[1-9])$";

    String message() default "账单日格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
