package com.antonionascimento.voting_api.service;

import com.antonionascimento.voting_api.dtos.requests.VoteRequestDTO;

public interface VoteService {
    Long registerVote(VoteRequestDTO request, String voterId);
}
