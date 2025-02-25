package com.example.ticket_support.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ticket_support.enums.Role;
import com.example.ticket_support.security.SecurityParams;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtils implements TokenUtils {
    @Override
    public String createAccessToken(String username, Role role) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, SecurityParams.EXPIRATION);

        return JWT.create()
                .withSubject(username)
                .withIssuer(SecurityParams.ISSUER)
                .withExpiresAt(calendar.getTime())
                .withArrayClaim("roles", new String[]{role.toString()})
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();

        return verifier.verify(token);
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT decodedJWT = verifyToken(token);

        return decodedJWT.getSubject();
    }

    @Override
    public List<String> getRolesName(String token) {
        DecodedJWT decodedJWT = verifyToken(token);

        return decodedJWT.getClaim("roles").asList(String.class);
    }
}
