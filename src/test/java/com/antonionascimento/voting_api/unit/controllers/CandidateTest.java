package com.antonionascimento.voting_api.unit.controllers;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.controllers.CandidateController;
import com.antonionascimento.voting_api.service.CandidateService;
import com.antonionascimento.voting_api.entities.Candidate;

@WebMvcTest(CandidateController.class)
@Import(SecurityException.class)
public class CandidateTest {

    @MockitoBean
    CandidateService candidateService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should return all candidates successfully")
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void shouldReturnAllCandidatesSuccessfully() throws Exception {
        List<Candidate> mockedCandidates = List.of(
            new Candidate(1l, "candidate1"),
            new Candidate(2l, "candidate2")
        );

        doReturn(mockedCandidates).when(candidateService).getCandidates();
        
        ResultActions response = mockMvc.perform(get("/candidates"));

        response.andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(mockedCandidates.size()))
        .andExpect(jsonPath("$[0].id").value(mockedCandidates.get(0).getId()))
        .andExpect(jsonPath("$[0].name").value(mockedCandidates.get(0).getName()));

        verify(candidateService, times(1)).getCandidates();
    }

    @Test
    @DisplayName("Should throw UnauthorizedException if an unregistered user tries to get candidates")
    public void shouldThrowUnauthorizedExceptionIfAnUnregisteredUserTriesToGetCandidates() throws Exception {
        List<Candidate> mockedCandidates = List.of(
            new Candidate(1l, "candidate1"),
            new Candidate(2l, "candidate2")
        );

        doReturn(mockedCandidates).when(candidateService).getCandidates();
        
        ResultActions response = mockMvc.perform(get("/candidates"));

        response.andExpect(status().isUnauthorized());

        verify(candidateService, times(0)).getCandidates();
    }

}
