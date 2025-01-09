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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.antonionascimento.voting_api.dtos.requests.LoginRequestDTO;
import com.antonionascimento.voting_api.entities.Role;
import com.antonionascimento.voting_api.entities.User;
import com.antonionascimento.voting_api.exceptions.UnauthorizedException;
import com.antonionascimento.voting_api.repository.UserRepository;
import com.antonionascimento.voting_api.security.JWTsigner;
import com.antonionascimento.voting_api.service.impl.LoginServiceImpl;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JWTsigner jwTsigner;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    LoginServiceImpl loginService;

    @Nested
    public class loginUser {

        private User createUser(String username, String password) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(username);
            user.setPassword(password);
    
            Role role = new Role();
            role.setId(2l);
            role.setName(Role.roleType.USER.name());
    
            user.setRoles(Set.of(role));
            return user;
        }
        
        @Test
        @DisplayName("Should authenticate User sucessfully and return token")
        public void shouldAuthenticateUserSucessfullyAndReturnToken(){
            User mockedUser = createUser("User", "HashedPass");
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("User", "passUser");

            doReturn(Optional.of(mockedUser)).when(userRepository).findByUsername(loginRequestDTO.username());
            doReturn(true).when(bCryptPasswordEncoder).matches(loginRequestDTO.password(), mockedUser.getPassword());
            doReturn("JwtToken").when(jwTsigner).sign(mockedUser, 300l);

            String token = loginService.authenticate(loginRequestDTO);

            assertEquals("JwtToken", token);
        }

        @Test
        @DisplayName("Should throw UnauthorizedException if user does not exist")
        public void shouldThrowUnauthorizedExceptionIfUserDoesNotExist(){
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("User", "passUser");
            doReturn(Optional.empty()).when(userRepository).findByUsername(loginRequestDTO.username());

            assertThrows(UnauthorizedException.class,
            () -> loginService.authenticate(loginRequestDTO));

            verify(bCryptPasswordEncoder, times(0)).matches(any(), any());
            verify(jwTsigner, times(0)).sign(any(), any());
        }

        @Test
        @DisplayName("Should throw UnauthorizedException if password does not matches")
        public void shouldThrowUnauthorizedExceptionIfPasswordDoesNotMatches(){
            User mockedUser = createUser("User", "HashedPass");
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("User", "passUser");
            
            doReturn(Optional.of(mockedUser)).when(userRepository).findByUsername(loginRequestDTO.username());
            doReturn(false).when(bCryptPasswordEncoder).matches(loginRequestDTO.password(), 
            mockedUser.getPassword());

            assertThrows(UnauthorizedException.class,
            () -> loginService.authenticate(loginRequestDTO));

            verify(jwTsigner, times(0)).sign(any(), any());
        }
    }
}
