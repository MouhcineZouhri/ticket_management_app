package com.example.ticket_support.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.repositories.UserRepository;
import com.example.ticket_support.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;

    private final UserRepository userRepository;

    public AuthorizationFilter(TokenUtils tokenUtils, UserRepository userRepository) {
        this.tokenUtils = tokenUtils;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(SecurityParams.JWT_HEADER_NAME);

        if (jwtToken == null || !jwtToken.startsWith(SecurityParams.HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtToken.substring(SecurityParams.HEADER_PREFIX.length());

        DecodedJWT decodedJWT = tokenUtils.verifyToken(jwt);

        List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);

        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        String username = decodedJWT.getSubject();

        User user = userRepository.findByEmail(username)
                .orElse(null);

        SecurityUser securityUser = new SecurityUser(user);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(securityUser, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }
}
