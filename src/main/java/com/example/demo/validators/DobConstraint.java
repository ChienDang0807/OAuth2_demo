package com.example.demo.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD}) // sử dụng cho pham vi nào
@Retention(RetentionPolicy.RUNTIME) // dùng lúc runtime hay compile time
@Constraint(validatedBy = { DobValidator.class }) // truyền vào class triển khai logic validator
public @interface DobConstraint {
    String message() default "Invalid date of bỉrth";

    int min();  // vì k set giá trị default nên lúc khai báo cần có gtri min

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
