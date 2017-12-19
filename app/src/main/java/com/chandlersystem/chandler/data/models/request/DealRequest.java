package com.chandlersystem.chandler.data.models.request;

public class DealRequest {
    private String accessToken;
    private String userId;

    public DealRequest() {
    }

    public DealRequest(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
