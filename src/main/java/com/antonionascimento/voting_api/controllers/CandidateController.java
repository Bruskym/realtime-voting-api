package com.antonionascimento.voting_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.antonionascimento.voting_api.entities.Candidate;
import com.antonionascimento.voting_api.service.CandidateService;

import org.springframework.web.bind.annotation.GetMapping;

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

}


