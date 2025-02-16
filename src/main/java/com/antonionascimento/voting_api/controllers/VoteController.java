package com.antonionascimento.voting_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.antonionascimento.voting_api.dtos.requests.VoteRequestDTO;
import com.antonionascimento.voting_api.dtos.responses.VoteResponseDTO;
import com.antonionascimento.voting_api.service.VoteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/vote")
public class VoteController {
    
    private VoteService voteService;

    public VoteController(VoteService voteService){
        this.voteService = voteService;
    }

    @PostMapping()
    public ResponseEntity<VoteResponseDTO> sendVote(@RequestBody VoteRequestDTO request, JwtAuthenticationToken token) {
        Long voteId = voteService.registerVote(request, token.getName());
        VoteResponseDTO voteResponse = new VoteResponseDTO(voteId, "Vote sent");

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(voteResponse);    
    }
}
