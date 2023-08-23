package com.gpt5.laundry.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationUtils {
    private final Validator validator;

    public <T> void validate(T obj) {
        Set<ConstraintViolation<T>> result = validator.validate(obj);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
    }
}
