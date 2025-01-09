package com.antonionascimento.voting_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.dtos.responses.RegisterResponseDTO;
import com.antonionascimento.voting_api.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO request) {
        String userId = userService.registerUser(request);
        RegisterResponseDTO response = new RegisterResponseDTO(userId);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
    }
}
