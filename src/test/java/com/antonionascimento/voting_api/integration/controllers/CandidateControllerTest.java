package com.antonionascimento.voting_api.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.antonionascimento.voting_api.config.TestConfigTest;
import com.antonionascimento.voting_api.controllers.CandidateController;
import com.antonionascimento.voting_api.dtos.requests.RegisterCandidateRequestDTO;
import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.entities.Role.roleType;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CandidateController.class)
@Import(TestConfigTest.class)
public class CandidateControllerTest {
    
    @MockitoBean
    CandidateService candidateService;
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JWTsigner jwtSigner;

    @Autowired
    JwtEncoder jwtEncoder;

    private User createUser(String username, String password, Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        return user;
    }

    @Nested
    class postCandidates{

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterCandidateRequestDTO request = new RegisterCandidateRequestDTO("candidate 1");

        @Test
        @DisplayName("should register new candidate if user role is Admin")
        public void shouldRegisterNewCandidateIfUserRoleIsAdmin() throws Exception{
            Role role = new Role(roleType.ADMIN.getRoleId(), roleType.ADMIN.name());
            User admin = createUser("admin", "123", role);
            String token = jwtSigner.sign(admin, 300l);
         
            doReturn("mockedUUID").when(candidateService).registerCandidate(request);

            ResultActions response = mockMvc.perform(post("/candidates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));
            
            response.andExpect(status().isCreated())
            .andExpect(jsonPath("$.candidateId").value("mockedUUID"));

            verify(candidateService, times(1)).registerCandidate(any(RegisterCandidateRequestDTO.class));
        }

        @Test
        @DisplayName("should throw Forbidden Status code if user role is not admin")
        public void shouldThrowUnauthorizedStatusCodeIfUserRoleIsNotAdmin() throws Exception{
            Role role = new Role(roleType.USER.getRoleId(), roleType.USER.name());
            User user = createUser("user1", "123", role);
            String token = jwtSigner.sign(user, 300l);

            ResultActions response = mockMvc.perform(post("/candidates")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

            response.andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message")
            .value("You do not have permission to access this resource."));
        }
    }

    @Nested 
    class getCandidates{

        List<Candidate> mockedCandidates = List.of(
            new Candidate(UUID.randomUUID(), "candidate1"),
            new Candidate(UUID.randomUUID(), "candidate2")
        );

        @Test
        @DisplayName("should return all candidates for authenticate user") 
        public void shouldReturnAllCandidatesForAuthenticateUser() throws Exception{
            Role role = new Role(roleType.USER.getRoleId(), roleType.USER.name());
            User user = createUser("User", "123", role);
            String token = jwtSigner.sign(user, 300l);

            doReturn(mockedCandidates).when(candidateService).getCandidates();
            
            ResultActions response = mockMvc.perform(get("/candidates")
            .header(HttpHeaders.AUTHORIZATION,"Bearer " + token));

            response.andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(mockedCandidates.size()))
            .andExpect(jsonPath("[0].id").value(mockedCandidates.get(0).getId().toString()))
            .andExpect(jsonPath("[0].name").value(mockedCandidates.get(0).getName()));
    
            verify(candidateService, times(1)).getCandidates();
        }   
    }
}
