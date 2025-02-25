package com.example.ticket_support.security;

public class SecurityParams {
    public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="zouhri.com";
    public static final int EXPIRATION=10*24*3600;
    public static final String HEADER_PREFIX="Bearer ";
    public static final String ISSUER = "http://localhost:8080";
}
