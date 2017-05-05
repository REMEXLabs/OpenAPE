package org.openape.server.auth;

import org.openape.api.Messages;

/**
 * RFC 6749 compliant access token response.
 */
public class TokenResponse {

    private String accessToken;
    private String expiresIn;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
        this.expiresIn = Messages.getString("Auth.TokenExpirationTimeInMinutes");
    }

    public TokenResponse(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
