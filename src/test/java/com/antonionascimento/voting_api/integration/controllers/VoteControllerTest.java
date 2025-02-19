package com.antonionascimento.voting_api.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.config.PermissionTestConfigTest;
import com.antonionascimento.voting_api.controllers.VoteController;
import com.antonionascimento.voting_api.dtos.requests.VoteRequestDTO;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.entities.Role.roleType;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(VoteController.class)
@Import(PermissionTestConfigTest.class)
public class VoteControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    VoteService voteService;

    @Autowired
    JWTsigner jwtSigner;

    private User createUser(String username, String password, Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        return user;
    }

    @Nested
    class testVote{
        
        VoteRequestDTO voteRequestDTO = new VoteRequestDTO(UUID.randomUUID().toString());
        
        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        @DisplayName("should register new vote if user is registered")
        public void shouldRegisterNewVoteIfUserIsRegistered() throws Exception{
            Role role = new Role(roleType.USER.getRoleId(), roleType.USER.name());
            User user = createUser("user", "password", role);
            String token = jwtSigner.sign(user, 300l);

            Long mockedVoteId = 1l;

            doReturn(mockedVoteId).when(voteService).registerVote(voteRequestDTO, user.getId().toString());

            ResultActions perform = mockMvc.perform(post("/vote")
                            .header("Authorization", "Bearer " + token)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(voteRequestDTO)));

            perform.andExpect(status().isCreated())
            .andExpect(jsonPath("$.voteId").value(mockedVoteId))
            .andExpect(jsonPath("$.message").value("Vote sent"));

            verify(voteService, times(1)).registerVote(any(), any());
        }

        @Test
        @DisplayName("should deny vote when user is not registered")
        public void shouldDenyVoteWhenUserIsNotRegistered() throws Exception{

            ResultActions perform = mockMvc.perform(post("/vote")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(voteRequestDTO)));

            perform.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("User not registered to vote"));

            verify(voteService, times(0)).registerVote(any(), any());
        }
    }
}
