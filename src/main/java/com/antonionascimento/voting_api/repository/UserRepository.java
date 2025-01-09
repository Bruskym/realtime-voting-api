package com.antonionascimento.voting_api.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonionascimento.voting_api.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByUsername(String username);    
}
