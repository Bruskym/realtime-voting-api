package com.antonionascimento.voting_api.config;

import org.springframework.context.annotation.Import;

import com.antonionascimento.voting_api.security.JWTsignerImpl;

@Import({SecurityConfig.class, 
    JwtConfigTest.class, 
    GlobalExceptionHandlerConfig.class,
    JWTsignerImpl.class})
public class PermissionTestConfigTest {}
