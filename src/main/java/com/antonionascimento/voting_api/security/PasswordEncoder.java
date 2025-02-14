package com.antonionascimento.voting_api.security;

public interface PasswordEncoder {
    String encode(String password);
    Boolean matches(String password, String encodedPassword);
}
