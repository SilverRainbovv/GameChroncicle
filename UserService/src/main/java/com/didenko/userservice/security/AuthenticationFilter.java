package com.didenko.userservice.security;

import com.didenko.userservice.dto.LoginDto;
import com.didenko.userservice.dto.UserReadDto;
import com.didenko.userservice.entity.User;
import com.didenko.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

        private UserService userService;
        private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            LoginDto loginCreds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginCreds.username(), loginCreds.password()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String username = ((User)authResult.getPrincipal()).getUsername();
        UserReadDto userReadDto = userService.getUserByUsername(username);

        //TODO set normal phrase
        String secretPhrase = "fiywgqrifgwqifgiqygrfiqgeifygqeirfgiqeoygroieqrgfiqgeigqeiorfygoqieryE123456789";
        byte[] phraseBytes = secretPhrase.getBytes();

        Key secretKey = Keys.hmacShaKeyFor(phraseBytes);


        String token = Jwts.builder()
                .subject(userReadDto.getUUID())
                .expiration(Date.from(Instant.now().plusMillis(3600000)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("userId", userReadDto.getUUID());
    }
}