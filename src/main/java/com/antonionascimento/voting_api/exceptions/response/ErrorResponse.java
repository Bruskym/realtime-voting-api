package com.antonionascimento.voting_api.exceptions.response;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    
    private int status;
    private String message;
    private Instant timestamp;

}

