package com.example.demo.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator  implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;


    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if(Objects.isNull(localDate)){
            return  true;
        }

        long year = ChronoUnit.YEARS.between(localDate,LocalDate.now()); // xác định giữa thời điểm hien tai voi gia tri truyen vao
                                                                            // là bao nhiêu năm


        return year >= min;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
