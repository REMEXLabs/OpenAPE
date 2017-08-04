package org.openape.server.auth;

/**
 * Exception to be thrown if a user can not be authorized.
 */
public class UnauthorizedException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1987250205279214600L;

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(final String message) {
		super(message);
	}

	public UnauthorizedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(final Throwable cause) {
		super(cause);
	}

}
