package com.antonionascimento.voting_api.integration.controllers;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.config.GlobalExceptionHandlerConfig;
import com.antonionascimento.voting_api.config.SecurityConfig;
import com.antonionascimento.voting_api.controllers.LoginController;
import com.antonionascimento.voting_api.controllers.UserController;
import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.exceptions.ConflictException;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.service.LoginService;
import com.antonionascimento.voting_api.service.UserService;

@WebMvcTest({LoginController.class, UserController.class})
@Import({SecurityConfig.class, GlobalExceptionHandlerConfig.class})
public class GlobalExceptionHandlerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoginService loginService;

    @MockitoBean
    UserService userService;

    @Test
    @DisplayName("should return 401 when UserUnauthorized is thrown")
    public void shouldReturn401WhenUserUnauthorizedIsThrown() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("Unauthorized user", "12345");

        doThrow(new UnauthorizedException("Invalid credentials")).when(loginService).authenticate(loginRequestDTO);

        ResultActions response = mockMvc.perform(post("/login")
        .content(loginRequestDTO.toJsonString())
        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    @DisplayName("should return 409 when ConflictException is thrown")
    public void shouldReturn409WhenConflictExceptionIsThrown() throws Exception{
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("already exists user", "123");

        doThrow(new ConflictException("user already exists")).when(userService).registerUser(registerRequestDTO);

        ResultActions response = mockMvc.perform(post("/users/register")
        .content(registerRequestDTO.toJsonString())
        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("user already exists"));
    }

}
