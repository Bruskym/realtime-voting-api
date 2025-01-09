package com.antonionascimento.voting_api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.antonionascimento.voting_api.exceptions.ConflictException;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.exceptions.response.ErrorResponseBuilder;

@ControllerAdvice
public class GlobalExceptionHandlerConfig extends ResponseEntityExceptionHandler{

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> allUnauthorizedExceptionHandler(Exception exception){
        String errorMessage = exception.getMessage();
        
        return ErrorResponseBuilder.builder()
        .status(HttpStatus.UNAUTHORIZED)
        .message(errorMessage)
        .build();
    }

    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<Object> allConflictExceptionHandler(Exception exception){
        String errorMessage = exception.getMessage();
        
        return ErrorResponseBuilder.builder()
        .status(HttpStatus.CONFLICT)
        .message(errorMessage)
        .build();
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception exception) {
    return ErrorResponseBuilder.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .message("An unexpected error occurred.")
        .build();
    }

}
