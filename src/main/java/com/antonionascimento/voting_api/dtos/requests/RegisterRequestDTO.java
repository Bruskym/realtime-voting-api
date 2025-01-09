package com.antonionascimento.voting_api.dtos.requests;

public record RegisterRequestDTO(String username, String password) {
    public String toJsonString(){
        return """
                {
                    "username": "%s",
                    "password": "%s"
                }
        """.formatted(username, password);
    }
}
