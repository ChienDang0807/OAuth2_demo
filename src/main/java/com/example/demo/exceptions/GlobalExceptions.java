package com.example.demo.exceptions;

import com.example.demo.dto.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

// khai báo Spring khi có exception tap trung xu li o day
@ControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ResponseData<?>> handingRuntimeException(Exception exception){
        return ResponseEntity.badRequest().body(new ResponseData<>(ErrorCode.UNCATEGORIZED_EXCEPTIONS.getCode(), ErrorCode.UNCATEGORIZED_EXCEPTIONS.getMessage()));
    }

    @ExceptionHandler(value = AppExceptions.class)
    ResponseEntity<ResponseData<?>> handingAppExceptions(AppExceptions exception){
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                             .body(new ResponseData<>(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ResponseData<?>> handlingAccessDenied(AccessDeniedException exception){
        ErrorCode errorCode=ErrorCode.UNAUTHORIZED;
        return  ResponseEntity.status(errorCode.getStatusCode())
                              .body(new ResponseData<>(errorCode.getCode(), errorCode.getMessage()));

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ResponseData<?>> handingValidation (MethodArgumentNotValidException exception){
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        return ResponseEntity.badRequest().body(new ResponseData<>(errorCode.getCode(),errorCode.getMessage()));
    }
}
