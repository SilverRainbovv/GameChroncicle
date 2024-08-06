package com.didenko.userservice.security;

import com.didenko.userservice.dto.LoginDto;
import com.didenko.userservice.dto.UserReadDto;
import com.didenko.userservice.entity.User;
import com.didenko.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            LoginDto creds = new ObjectMapper().readValue(req.getInputStream(), LoginDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.username(), creds.password(), new ArrayList<>())
            );

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User)auth.getPrincipal()).getUsername();
        UserReadDto userDetails = userService.getUserByUsername(userName);

        String token = generateToken(userDetails);

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUUID());
    }

    private String generateToken(UserReadDto userDetails) {
        String tokenSecret = env.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        Instant now = Instant.now();

        return Jwts.builder().setSubject(userDetails.getUUID())
                .setExpiration(Date.from(now.plusMillis(3600000L)))
                .setIssuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();
    }
}