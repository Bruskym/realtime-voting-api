package com.antonionascimento.voting_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonionascimento.voting_api.entities.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, UUID>{
    Optional<Candidate> findCandidateByName(String name);
}
