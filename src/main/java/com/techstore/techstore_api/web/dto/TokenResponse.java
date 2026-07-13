package com.techstore.techstore_api.web.dto;

public class TokenResponse {
    private String tokenType = "Bearer";
    private String token;

    public TokenResponse(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public String getToken() { return token; }
}