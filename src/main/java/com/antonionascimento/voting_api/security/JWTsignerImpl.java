package com.antonionascimento.voting_api.security;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.antonionascimento.voting_api.entities.User;

@Component
public class JWTsignerImpl implements JWTsigner{

    private JwtEncoder jwtEncoder;

    public JWTsignerImpl(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String sign(User user, Long expiresIn) {
        
        Instant now = Instant.now();

        List<String> userRoles = user.getRoles().stream().map(
            role -> role.getName()
        ).collect(Collectors.toList());

        JwtEncoderParameters jwtModel = JwtEncoderParameters.from(
            JwsHeader.with(SignatureAlgorithm.RS256).build(),
            JwtClaimsSet.builder()
            .issuer("votingAPI")
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("roles", userRoles)
            .build());

        Jwt encodedToken = jwtEncoder.encode(jwtModel);
        
        return encodedToken.getTokenValue();
    }

}


