package org.openape.server.auth;

import com.google.gson.annotations.SerializedName;

/**
 * RFC 6749 compliant access token response.
 */
public class TokenResponse {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private String expiresIn;

    public TokenResponse(final String accessToken, final String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getExpiresIn() {
        return this.expiresIn;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresIn(final String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
