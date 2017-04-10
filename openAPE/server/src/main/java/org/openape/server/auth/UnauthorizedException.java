package org.openape.server.auth;

public class UnauthorizedException extends Exception {

    public UnauthorizedException() { super(); }
    public UnauthorizedException(String message) { super(message); }
    public UnauthorizedException(String message, Throwable cause) { super(message, cause); }
    public UnauthorizedException(Throwable cause) { super(cause); }

}
