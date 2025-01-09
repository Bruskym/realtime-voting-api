package com.antonionascimento.voting_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.antonionascimento.voting_api.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findRoleByName(String name);
}
