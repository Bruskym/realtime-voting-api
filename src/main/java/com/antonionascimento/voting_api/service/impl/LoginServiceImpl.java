package com.antonionascimento.voting_api.service.impl;

import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.repository.UserRepository;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.security.PasswordEncoder;
import com.antonionascimento.voting_api.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTsigner jwtSigner;

    public LoginServiceImpl(UserRepository userRepository, 
    PasswordEncoder passwordEncoder, 
    JWTsigner jwtSigner) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSigner = jwtSigner;
    }

    @Override
    public String authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.username()).orElseThrow(
        () -> new UnauthorizedException("Invalid Credentials"));
        
        if(!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())){
            throw new UnauthorizedException("Invalid Credentials");
        }
        
        String jwtToken = jwtSigner.sign(user, 300l);

        return jwtToken;
    }

}
