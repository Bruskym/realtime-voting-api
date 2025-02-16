package com.antonionascimento.voting_api.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.antonionascimento.voting_api.exceptions.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
        throws IOException {
        resolver.resolveException(request, response, null, new UnauthorizedException("User not registered to vote"));
    }
}
