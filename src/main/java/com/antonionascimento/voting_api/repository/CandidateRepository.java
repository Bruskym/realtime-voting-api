package com.antonionascimento.voting_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonionascimento.voting_api.entities.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long>{}
