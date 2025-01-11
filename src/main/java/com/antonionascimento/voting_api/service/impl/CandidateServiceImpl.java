package com.antonionascimento.voting_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.dtos.requests.RegisterCandidateRequestDTO;
import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.exceptions.ConflictException;
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

    @Override
    public String registerCandidate(RegisterCandidateRequestDTO request) {
        candidateRepository.findCandidateByName(request.name()).ifPresent(
            candidate -> {throw new ConflictException("this candidate already registered!");});
        
        Candidate candidate = new Candidate();
        candidate.setName(request.name());
        
        Candidate savedCandidate = candidateRepository.save(candidate);

        return savedCandidate.getId().toString();
    }
    
}
