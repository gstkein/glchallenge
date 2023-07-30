package com.gsteren.glchallenge.exception;

public class CustomException extends RuntimeException{

    String errorCode;

    public CustomException(String message) {
        super(message);
    }

}
