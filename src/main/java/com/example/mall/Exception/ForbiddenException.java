package com.example.mall.Exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
