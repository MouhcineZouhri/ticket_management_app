package com.example.ticket_support.security;

import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.Role;
import com.example.ticket_support.mappers.UserMapper;
import com.example.ticket_support.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    private final UserMapper userMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userMapper = userMapper;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User appUser = new ObjectMapper().readValue(request.getInputStream(),User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getEmail(),appUser.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser securityUser =(SecurityUser) authResult.getPrincipal();
        Role role = authResult.getAuthorities().stream().map(
                grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority())
        ).findFirst().orElse(null);

        String accessToken = tokenUtils.createAccessToken(securityUser.getUsername(), role);

        // configuration
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // add access_token header
        response.addHeader("access_token",accessToken);

        // add user information to response
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();

        objectMapper.writeValue(out, userMapper.userResponseFromUser(securityUser.getUser()));
    }
}
