package org.openape.api.auth;

import com.fasterxml.jackson.annotation.JsonSetter;
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

    public TokenResponse() {

    }

    public String getAccessToken() {
        return this.accessToken;
    }

    @JsonSetter("access_token")
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return this.expiresIn;
    }

    @JsonSetter("expires_in")
    public void setExpiresIn(final String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
