package com.antonionascimento.voting_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.dtos.responses.LoginResponseDTO;
import com.antonionascimento.voting_api.service.LoginService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        String jwtToken = loginService.authenticate(loginRequestDTO);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken);

        return ResponseEntity.ok().body(loginResponseDTO);
    }

}
