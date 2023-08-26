package com.gpt5.laundry.controller;

import com.gpt5.laundry.model.response.CommonResponse;
import com.gpt5.laundry.util.BadRequestUtils;
import com.gpt5.laundry.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException exception) {
        Map<String, Object> errors = ValidationUtils.mapConstraintViolationException(exception.getConstraintViolations());
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(commonResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .statusCode(exception.getStatus().value())
                .errors(exception.getReason())
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(exception.getStatus())
                .body(commonResponse);
    }

    @ExceptionHandler(BadRequestUtils.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestUtils exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errors(exception.getErrors())
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(commonResponse);
    }

}
