package com.antonionascimento.voting_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.repository.CandidateRepository;
import com.antonionascimento.voting_api.service.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService{

    private CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository){
        this.candidateRepository = candidateRepository;
    }

    @Override
    public List<Candidate> getCandidates() {
        return candidateRepository.findAll();
    }
    
}
