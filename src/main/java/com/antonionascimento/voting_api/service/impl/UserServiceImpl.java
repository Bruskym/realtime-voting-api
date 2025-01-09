package com.antonionascimento.voting_api.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.ConflictException;
import com.antonionascimento.voting_api.exceptions.NotFoundException;
import com.antonionascimento.voting_api.repository.RoleRepository;
import com.antonionascimento.voting_api.repository.UserRepository;
import com.antonionascimento.voting_api.security.PasswordEncoder;
import com.antonionascimento.voting_api.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public String registerUser(RegisterRequestDTO request) {
        Role role = roleRepository.findRoleByName(Role.roleType.USER.name()).orElseThrow(
            () -> new NotFoundException("Role not found"));
        
        userRepository.findByUsername(request.username()).ifPresent(
            user -> { throw new ConflictException("This user already registered"); 
        });

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(encodedPassword);
        newUser.setRoles(Set.of(role));

        User user = userRepository.save(newUser);
        
        return user.getId().toString();
    }
    
}
