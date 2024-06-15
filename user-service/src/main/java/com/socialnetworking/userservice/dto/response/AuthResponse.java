package com.socialnetworking.userservice.dto.response;


import com.socialnetworking.shared_service.dto.response.BaseResponse;
import lombok.Data;

@Data
public class AuthResponse extends BaseResponse {
    private String accessToken;

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    private String tokenType = "Bearer ";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
