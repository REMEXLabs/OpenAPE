package org.openape.server.auth;

/**
 * An RFC 6749 section 5.2 compliant OAuth error object.
 */

public class AuthError {

    private String error;
    private String errorDescription;

    public AuthError(String error, String description) {
        this.error = error;
        this.errorDescription = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
