package org.example;

import org.example.models.Token;

public class TokenHolder {
    private final Token token;

    private static TokenHolder INSTANCE = new TokenHolder(null);

    private TokenHolder(Token token) {
        this.token = token;
    }

    public static TokenHolder getInstance() {
        return INSTANCE;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        INSTANCE = new TokenHolder(token);
    }
}
