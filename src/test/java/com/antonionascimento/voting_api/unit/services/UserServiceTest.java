package com.antonionascimento.voting_api.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.ConflictException;
import com.antonionascimento.voting_api.repository.RoleRepository;
import com.antonionascimento.voting_api.repository.UserRepository;
import com.antonionascimento.voting_api.security.PasswordEncoder;
import com.antonionascimento.voting_api.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock 
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private User createUser(String username, String password, Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        return user;
    }
    
    @Nested
    class registerNewUserTests{

        RegisterRequestDTO request = new RegisterRequestDTO("new user", "123");

        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);

        @Test
        @DisplayName("should register new user sucessfully")
        public void shouldRegisterNewUserSucessfully(){
            Role role = new Role(1, "USER");
            User savedUser = createUser(request.username(), "hashedPass", role);

            doReturn(Optional.of(role)).when(roleRepository).findRoleByName(Role.roleType.USER.name());
            doReturn(Optional.empty()).when(userRepository).findByUsername(request.username());
            doReturn("hashedPass").when(passwordEncoder).encode(request.password());

            doReturn(savedUser).when(userRepository).save(user.capture());
            
            String userId = userServiceImpl.registerUser(request);

            User captureValue = user.getValue();

            assertEquals(savedUser.getId().toString(), userId);
            assertEquals(savedUser.getUsername(), captureValue.getUsername());
            assertEquals(savedUser.getPassword(), captureValue.getPassword());

            verify(roleRepository, times(1)).findRoleByName("USER");
            verify(userRepository, times(1)).findByUsername(request.username());
            verify(passwordEncoder, times(1)).encode(request.password());
            verify(userRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should throw ConflictException if user already exists")
        public void shouldThrowConflictExceptionIfUserAlreadyExists(){
            Role role = new Role(1, "USER");
            User user = createUser(request.username(), "hashedPass", role);

            doReturn(Optional.of(role)).when(roleRepository).findRoleByName(Role.roleType.USER.name());
            doReturn(Optional.of(user)).when(userRepository).findByUsername(request.username());

            assertThrows(ConflictException.class, 
            () -> userServiceImpl.registerUser(request));

            verify(roleRepository, times(1)).findRoleByName("USER");
            verify(userRepository, times(1)).findByUsername(request.username());
            verify(passwordEncoder, times(0)).encode(request.password());
            verify(userRepository, times(0)).save(any());
        }
    }
}
