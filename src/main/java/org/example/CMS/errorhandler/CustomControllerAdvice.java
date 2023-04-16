package org.example.CMS.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException invalidInputException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(invalidInputException.getErrorCode().getCode())
                .message(invalidInputException.getErrorCode().getMessage())
                .timestamp(new Date()).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ErrorCode.CMS102.getCode())
                .message(errors.toString())
                .timestamp(new Date())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

}
