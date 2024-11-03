package com.example.demo.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Getter
public enum ErrorCode {
    USER_EXISTED (1001,"User existed",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002,"User is not found",HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTIONS (9999,"Uncategorized exceptions  ", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003,"Username not blank",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Password at least 8 characters",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED (1007,"You do not have permission",HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Invalid dob", HttpStatus.BAD_REQUEST)
    ;


    int  code;
    String message;
    HttpStatusCode statusCode;
}
