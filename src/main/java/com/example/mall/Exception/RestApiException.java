package com.example.mall.Exception;

public class RestApiException extends RuntimeException{
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
