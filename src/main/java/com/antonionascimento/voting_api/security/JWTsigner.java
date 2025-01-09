package com.antonionascimento.voting_api.security;

import com.antonionascimento.voting_api.entities.User;

public interface JWTsigner {
    String sign(User user, Long expiresIn);
}
