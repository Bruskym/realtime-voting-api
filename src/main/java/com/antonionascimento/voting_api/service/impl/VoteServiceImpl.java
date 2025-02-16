package com.antonionascimento.voting_api.service.impl;

import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.dtos.requests.VoteRequestDTO;
import com.antonionascimento.voting_api.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService{

    @Override
    public Long registerVote(VoteRequestDTO request, String voterId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerVote'");
    }
}
