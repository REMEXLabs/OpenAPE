package org.openape.api.auth;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.gson.annotations.SerializedName;
import org.openape.api.Messages;

import java.util.ResourceBundle;

/**
 * RFC 6749 compliant access token response.
 */
public class TokenResponse {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private String expiresIn;

    public TokenResponse(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public TokenResponse(){
    	
    }
    
    public String getAccessToken() {
        return accessToken;
    }

  @JsonSetter("access_token")  
  public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }
@JsonSetter("expires_in")
    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
