package com.example.demo.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppExceptions extends RuntimeException{

   ErrorCode errorCode;

    public AppExceptions(ErrorCode errorCode){
        super(errorCode.getMessage());
       this.errorCode = errorCode;
    }
}
