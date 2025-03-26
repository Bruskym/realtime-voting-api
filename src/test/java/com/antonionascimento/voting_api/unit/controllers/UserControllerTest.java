package com.antonionascimento.voting_api.unit.controllers;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.antonionascimento.voting_api.config.JwtConfigTest;
import com.antonionascimento.voting_api.config.SecurityConfig;
import com.antonionascimento.voting_api.controllers.UserController;
import com.antonionascimento.voting_api.dtos.requests.RegisterRequestDTO;
import com.antonionascimento.voting_api.service.UserService;
import com.antonionascimento.voting_api.utils.MessageSerializer;
import com.antonionascimento.voting_api.utils.Impl.JacksonJsonMessageSerializer;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtConfigTest.class, JacksonJsonMessageSerializer.class})
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    MessageSerializer messageSerializer;

    @Nested
    public class testRegisterUser{

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user", "password");

        @Test
        @DisplayName("should register new user sucessfully")
        public void shouldRegisterNewUserSucessfully() throws Exception{
            String mockedId = UUID.randomUUID().toString();

            doReturn(mockedId).when(userService).registerUser(registerRequestDTO);

            ResultActions result = mockMvc.perform(post("/users/register")
            .content(messageSerializer.serialize(registerRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId").value(mockedId));
        }
    }
}