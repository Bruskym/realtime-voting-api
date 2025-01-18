package com.antonionascimento.voting_api.integration.controllers;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.controllers.CandidateController;
import com.antonionascimento.voting_api.controllers.LoginController;
import com.antonionascimento.voting_api.controllers.UserController;
import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.dtos.requests.RegisterCandidateRequestDTO;
import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.ConflictException;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.service.CandidateService;
import com.antonionascimento.voting_api.service.LoginService;
import com.antonionascimento.voting_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.antonionascimento.voting_api.config.PermissionTestConfigTest;

@WebMvcTest({LoginController.class, UserController.class, CandidateController.class})
@Import(PermissionTestConfigTest.class)
public class GlobalExceptionHandlerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JWTsigner jwtSigner;

    @MockitoBean
    LoginService loginService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    CandidateService candidateService;

    ObjectMapper objectMapper = new ObjectMapper();

    private User createUser(String username, String password, Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        return user;
    }

    @Test
    @DisplayName("should return 401 when UserUnauthorized is thrown")
    public void shouldReturn401WhenUserUnauthorizedIsThrown() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("Unauthorized user", "12345");

        doThrow(new UnauthorizedException("Invalid credentials")).when(loginService).authenticate(loginRequestDTO);

        ResultActions response = mockMvc.perform(post("/login")
        .content(objectMapper.writeValueAsString(loginRequestDTO))
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
        .content(objectMapper.writeValueAsString(registerRequestDTO))
        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("user already exists"));
    }

    @Test
    @DisplayName("should return 403 when ForbiddenException is thrown")
    public void shoudReturn403WhenForbiddenExceptionIsThrown() throws Exception{
        Role forbiddenRole = new Role(Role.roleType.USER.getRoleId(), Role.roleType.USER.name());
        User user = createUser("forbiddenUser", "12345", forbiddenRole);
        String token = jwtSigner.sign(user, 300l);
        
        RegisterCandidateRequestDTO registerCandidateRequestDTO = new RegisterCandidateRequestDTO("candidate");

        ResultActions response = mockMvc.perform(post("/candidates")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .content(objectMapper.writeValueAsString(registerCandidateRequestDTO))
        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isForbidden());
    }
}
