package com.antonionascimento.voting_api.exceptions.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder{
    
    private ErrorResponse errorResponse;

    private ErrorResponseBuilder(){}

    public static ErrorResponseBuilder builder(){
        ErrorResponseBuilder instance = new ErrorResponseBuilder();
        instance.errorResponse = new ErrorResponse();
        instance.errorResponse.setTimestamp(Instant.now());

        return instance;
    }

    public ErrorResponseBuilder status(HttpStatus httpStatus){
        errorResponse.setStatus(httpStatus.value());
        return this;
    }

    public ErrorResponseBuilder message(String message){
        errorResponse.setMessage(message);
        return this;
    }

    public ResponseEntity<Object> build(){
        return ResponseEntity.status(errorResponse.getStatus())
        .body(errorResponse);
    }

}
