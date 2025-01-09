package com.antonionascimento.voting_api.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.repository.CandidateRepository;
import com.antonionascimento.voting_api.service.impl.CandidateServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Nested
    public class getCandidate{

        List<Candidate> mockedCandidates = List.of(
            new Candidate(1l, "candidate1"),
            new Candidate(2l, "candidate2")
        );

        @Mock
        CandidateRepository candidateRepository;

        @InjectMocks
        CandidateServiceImpl candidateServiceImpl;

        @Test
        @DisplayName("should return all candidates")
        public void shouldReturnAllCandidates(){
            doReturn(mockedCandidates).when(candidateRepository).findAll();

            List<Candidate> returnedCandidates = candidateServiceImpl.getCandidates();

            assertEquals(returnedCandidates.size(), mockedCandidates.size());
            verify(candidateRepository, times(1)).findAll();
        }
    }

}
