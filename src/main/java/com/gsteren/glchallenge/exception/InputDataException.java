package com.gsteren.glchallenge.exception;

public class InputDataException extends CustomException {

    String errorCode;

    public InputDataException(String message) {
        super(message);
    }

}
