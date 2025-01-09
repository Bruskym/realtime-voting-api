package com.antonionascimento.voting_api.unit.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.config.SecurityConfig;
import com.antonionascimento.voting_api.controllers.LoginController;
import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.service.LoginService;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    LoginService loginService;
    
    @Nested
    public class testLoginUser{
        
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user", "password");

        @Test
        @DisplayName("should login successfully when user is registered")
        public void shouldLogInSuccessfullyWhenUserIsRegistered() throws Exception {
            // Arrange
            
            doReturn("mockedToken").when(loginService).authenticate(loginRequestDTO);

            // Act
            ResultActions result = mockMvc.perform(post("/login")
            .content(loginRequestDTO.toJsonString())
            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mockedToken"));
        }

    }
}


