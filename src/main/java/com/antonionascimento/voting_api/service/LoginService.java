package com.antonionascimento.voting_api.service;

import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;

public interface LoginService {
    String authenticate(LoginRequestDTO loginRequestDTO);
}
