package com.korit.library.aop;

import com.korit.library.exception.CustomValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ValidationAop {

    @Pointcut("com.korit.library.aop.annotation.validAspect")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        BeanPropertyBindingResult bindingResult = null;

        for(Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
            }
        }

        if(bindingResult.hasErrors())) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldError().forEach(error -> {
               errorMap.put(error.getField(), error.getDefaultMessage());
            });

            throw new CustomValidationException(errorMap);
        }

        return proceedingJoinPoint.proceed();
    }

}
