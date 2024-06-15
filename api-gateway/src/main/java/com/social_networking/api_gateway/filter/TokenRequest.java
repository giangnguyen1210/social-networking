package com.social_networking.api_gateway.filter;

import lombok.Data;

@Data
public class TokenRequest {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }
}
