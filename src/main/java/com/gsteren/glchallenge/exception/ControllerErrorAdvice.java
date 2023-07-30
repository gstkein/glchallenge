package com.gsteren.glchallenge.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.gsteren.glchallenge.dto.ErrorDTO;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleAll(Exception exception, WebRequest request) {
        return getErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        
//    	String message = exception.getMessage().substring(0)
    	return getErrorDTO(HttpStatus.BAD_REQUEST.value(),exception.getFieldError().getDefaultMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodNotSupportedException(Exception exception, WebRequest request) {
        return getErrorDTO(HttpStatus.BAD_REQUEST.value(),exception);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleExpiredJwtException(ExpiredJwtException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthenticationException(AuthenticationException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }
    

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleBadCredentialsException(BadCredentialsException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorizedException(UnauthorizedException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        String message ="BAD_REQUEST";
        if (exception.getMessage().contains("JSON parse error")) {
            message = "INVALID_JSON";
        }
        return getErrorDTO(HttpStatus.BAD_REQUEST.value(),
                message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAccessDeniedException(
            AccessDeniedException exception) {
        return getErrorDTO(HttpStatus.UNAUTHORIZED.value(),exception);
    }

    @ExceptionHandler(InputDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleInputDataException(InputDataException exception, WebRequest request) {
        return getErrorDTO(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());
    }


    private ErrorDTO getErrorDTO(int errorCode, Exception exception) {
        log.error("Exception While Processing Request ", exception);
        ErrorDTO errorDTO = new ErrorDTO();
        ErrorDTO.ErrorEntry errorEntry1 = new ErrorDTO.ErrorEntry(
                LocalDateTime.now(), 
                errorCode,
                exception.getMessage()
        );
        List<ErrorDTO.ErrorEntry> errorList = new ArrayList<>();
        errorList.add(errorEntry1);
        
        errorDTO.setError(errorList);
        return errorDTO;
    }
    
    private ErrorDTO getErrorDTO(int errorCode, String errorMessage) {
        log.error("Exception While Processing Request ", errorMessage);
        ErrorDTO errorDTO = new ErrorDTO();
        ErrorDTO.ErrorEntry errorEntry1 = new ErrorDTO.ErrorEntry(
                LocalDateTime.now(), 
                errorCode,
                errorMessage
        );
        List<ErrorDTO.ErrorEntry> errorList = new ArrayList<>();
        errorList.add(errorEntry1);
        
        errorDTO.setError(errorList);
        return errorDTO;
    }

}
