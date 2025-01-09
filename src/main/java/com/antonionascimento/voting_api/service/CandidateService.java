package com.antonionascimento.voting_api.service;

import java.util.List;

import com.antonionascimento.voting_api.entities.Candidate;

public interface CandidateService {
    List<Candidate> getCandidates();
}
