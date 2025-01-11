package com.antonionascimento.voting_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.antonionascimento.voting_api.dtos.requests.RegisterCandidateRequestDTO;
import com.antonionascimento.voting_api.dtos.responses.RegisterCandidateResponseDTO;
import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.service.CandidateService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    private CandidateService candidateService;

    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.getCandidates();
        return ResponseEntity.ok().body(candidates);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RegisterCandidateResponseDTO> registerCandidate(@RequestBody RegisterCandidateRequestDTO request) {
        String candidateId = candidateService.registerCandidate(request);
        RegisterCandidateResponseDTO response = new RegisterCandidateResponseDTO(candidateId);
                
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


