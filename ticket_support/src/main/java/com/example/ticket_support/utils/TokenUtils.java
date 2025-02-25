package com.example.ticket_support.utils;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ticket_support.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface TokenUtils {
    String createAccessToken(String username, Role role);

    DecodedJWT verifyToken(String token);
    String getUsername(String token);
    List<String> getRolesName(String token);
}
