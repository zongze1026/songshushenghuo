package com.yitkeji.songshushenghuo.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class RegValidator implements ConstraintValidator<Annotation, String> {

    private Pattern pattern = null;
    boolean allowEmpty = false;

    @Override
    public void initialize(Annotation reg) {
        Object object = executeMethod(reg, "value");
        if (object != null) {
            pattern = Pattern.compile(object.toString());
        }
        object = executeMethod(reg, "allowEmpty");
        if (object != null) {
            allowEmpty = (Boolean) object;
        }
    }


    private Object executeMethod(Annotation reg, String methodName) {
        try {
            Method method = reg.getClass().getMethod(methodName);
            Object object = method.invoke(reg);
            return object;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(str) && allowEmpty) {
            return true;
        }
        if (pattern == null || StringUtils.isEmpty(str)) {
            return false;
        }
        return pattern.matcher(str).matches();
    }
}
