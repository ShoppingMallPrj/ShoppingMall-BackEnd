package com.example.mall.Exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
