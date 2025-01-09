package com.antonionascimento.voting_api.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.repository.UserRepository;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTsigner jwtSigner;

    public LoginServiceImpl(UserRepository userRepository, 
    BCryptPasswordEncoder bCryptPasswordEncoder, 
    JWTsigner jwtSigner) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtSigner = jwtSigner;
    }

    @Override
    public String authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.username()).orElseThrow(
        () -> new UnauthorizedException("Invalid Credentials"));
        
        if(!bCryptPasswordEncoder.matches(loginRequestDTO.password(), user.getPassword())){
            throw new UnauthorizedException("Invalid Credentials");
        }
        
        String jwtToken = jwtSigner.sign(user, 300l);

        return jwtToken;
    }

}
