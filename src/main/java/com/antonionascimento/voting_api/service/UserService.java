package com.antonionascimento.voting_api.service;

import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;

public interface UserService {
    String registerUser(RegisterRequestDTO request);
}
