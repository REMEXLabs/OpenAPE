package org.openape.server.auth;

/**
 * Exception to be thrown if a user can not be authorized.
 */
public class UnauthorizedException extends Exception {
    private static final long serialVersionUID = -1987250205279214600L;
    
    public UnauthorizedException() { super(); }
    public UnauthorizedException(String message) { super(message); }
    public UnauthorizedException(String message, Throwable cause) { super(message, cause); }
    public UnauthorizedException(Throwable cause) { super(cause); }

}
