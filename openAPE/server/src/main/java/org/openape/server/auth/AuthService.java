package org.openape.server.auth;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.jboss.resteasy.spi.NotFoundException;
import org.openape.api.DatabaseObject;
import org.openape.api.auth.TokenResponse;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

/**
 * Authorize users and generate tokens.
 */

public class AuthService {
	private static Logger logger = LoggerFactory.getLogger(AuthService.class);
	private static final ResourceBundle properties = ResourceBundle.getBundle("config/auth");
	private static final String JWT_SIGNATURE = AuthService.properties.getString("JwtSignature");
	private static final String EXPIRATION_TIME = AuthService.properties.getString("TokenExpirationTimeInMinutes");
	private final Config config = new AuthConfigFactory(AuthService.JWT_SIGNATURE).build();
	private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

	/**
	 * Tries to authorize using the given credentials (username and password)
	 * and returns a JWT String for the user if the authorization was
	 * successful.
	 *
	 * @param username
	 *            username to use for the authorization
	 * @param password
	 *            password to use for the authorization
	 * @return Token String
	 * @throws UnauthorizedException
	 *             if user can not be authorized
	 */
	public String getToken(final String username, final String password) throws UnauthorizedException {
		final CommonProfile profile = this.authorizeUser(username, password);
		return AuthService.generateJwt(profile);
	}

	/**
	 * Tries to authorize using the given credentials (username and password)
	 * and returns a TokenResponse as JSON.
	 *
	 * @param username
	 *            username to use for the authorization
	 * @param password
	 *            password to use for the authorization
	 * @return Token String
	 * @throws UnauthorizedException
	 *             if user can not be authorized
	 */
	public String getJSONToken(final String username, final String password) throws UnauthorizedException {
		final TokenResponse response = new TokenResponse(this.getToken(username, password),
				AuthService.EXPIRATION_TIME);
		return new Gson().toJson(response);
	}

	/**
	 * Authorize any profile that has the given role.
	 *
	 * @param role
	 *            Name of the role that should be authorized.
	 * @return PAC4J SecurityFilter containing the authorization for the given
	 *         role.
	 */
	public SecurityFilter authorize(final String role) {
		switch (role) {
		// Authenticate only if role "admin" is present
		case "admin":
			return new SecurityFilter(this.config, "HeaderClient", "adminOnly");
		// Authenticate if role "admin" or "user" is present
		case "user":
			return new SecurityFilter(this.config, "HeaderClient", "userAndAdmin");
		// Authenticate anyone, also anonymous users
		case "anonymous":
			return new SecurityFilter(this.config, "HeaderClient,AnonymousClient");
		// Require token, but authorize any role
		default:
			return new SecurityFilter(this.config, "HeaderClient");
		}
	}

	/**
	 * Get the profile of the currently authenticated user. Throws an exception
	 * if no user is authenticated.
	 *
	 * @param request
	 *            Spark request object
	 * @param response
	 *            Spark response object
	 * @return The CommonProfile for the authenticated user
	 * @throws UnauthorizedException
	 *             Will be thrown if no user is authenticated.
	 */
	public CommonProfile getAuthenticatedProfile(final Request request, final Response response)
			throws UnauthorizedException {
		final ProfileManager manager = new ProfileManager(new SparkWebContext(request, response));
		final Optional<CommonProfile> profile = manager.get(false);
		AuthService.logger.info("Profile: " + profile);
		if (profile.isPresent()) {
			return profile.get();
		} else {
			AuthService.logger.debug("Error: Couldn't find profile");
			throw new UnauthorizedException("Unauthorized");
		}
	}

	/**
	 * Get a OpenAPE user object for the currently authenticated user. Throws an
	 * exception if no user is authenticated.
	 *
	 * @param req
	 *            Spark request object
	 * @param res
	 *            Spark response object
	 * @return The User object representing the authenticated user.
	 * @throws UnauthorizedException
	 *             Will be thrown if no user is authenticated.
	 */
	public User getAuthenticatedUser(final Request req, final Response res) throws UnauthorizedException {
		return User.getFromProfile(this.getAuthenticatedProfile(req, res));
	}

	/**
	 * Check if the authenticated user has either the role `admin` or equals the
	 * provided owner.
	 *
	 * @param request
	 *            Spark request object
	 * @param response
	 *            Spark response object
	 * @param owner
	 *            The owner id to authorize against
	 * @throws UnauthorizedException
	 *             Will be thrown if authenticated user is no admin and also not
	 *             the owner
	 */
	public void allowAdminAndOwner(final Request request, final Response response, final String owner)
			throws UnauthorizedException {
		final CommonProfile profile = this.getAuthenticatedProfile(request, response);
		if (!this.isAdminOrOwner(profile, owner)) {
			throw new UnauthorizedException("You are not allowed to perform this operation");
		}
	}

	/**
	 * Check if the authenticated user has either the role `admin`, equals the
	 * provided owner or the resource is public.
	 *
	 * @param request
	 *            Spark request object
	 * @param response
	 *            Spark response object
	 * @param owner
	 *            The owner id to authorize against
	 * @param isPublic
	 *            Public state of the resource
	 * @throws UnauthorizedException
	 *             Will be thrown if non of the conditions are met.
	 */
	public void allowAdminOwnerAndPublic(final Request request, final Response response, final String owner,
			final boolean isPublic) throws UnauthorizedException {
		final CommonProfile profile = this.getAuthenticatedProfile(request, response);
		if (!(isPublic || this.isAdminOrOwner(profile, owner))) {
			throw new UnauthorizedException("You are not allowed to perform this operation");
		}
	}

	/**
	 * Creates string error containing an RFC 6749 section 5.2 compliant JSON
	 * form with the provided error and description.
	 *
	 * @param error
	 *            The error code.
	 * @param description
	 *            Human-readable error description.
	 * @return The JSON string error.
	 */
	public static String buildAuthErrorJSON(final String error, final String description) {
		final AuthError authError = new AuthError(error, description);
		return new Gson().toJson(authError);
	}

	/**
	 * Check if the provided profile has the `admin` role.
	 *
	 * @param profile
	 * @return True if admin role is present, otherwise false.
	 */
	private boolean isAdmin(final CommonProfile profile) {
		return profile.getRoles().contains("admin");
	}

	/**
	 * Check if the provided profile has either the role `admin` or is equal to
	 * the given owner.
	 *
	 * @param profile
	 *            The authenticated profile
	 * @param owner
	 *            The owner id.
	 * @return True if the profile is also the owner, otherwise false.
	 */
	private boolean isOwner(final CommonProfile profile, final String owner) {
		return profile.getId().equals(owner);
	}

	/**
	 * Check if the provided profile has either the role `admin` or is equal to
	 * the given owner.
	 *
	 * @param profile
	 *            The authenticated profile
	 * @param owner
	 *            The owner id.
	 * @return True if the profile is also the owner, otherwise false.
	 */
	private boolean isAdminOrOwner(final CommonProfile profile, final String owner) {
		return this.isAdmin(profile) || this.isOwner(profile, owner);
	}

	/**
	 * Generates a Json Web Token (JWT) for the given CommonProfile.
	 *
	 * @param profile
	 *            the profile the token should be created for
	 * @return a JWT as String
	 */
	private static String generateJwt(final CommonProfile profile) {
		profile.addAttribute("exp", AuthService.getExpirationDate()); // Expire
																		// token
																		// in X
																		// minutes
		profile.addAttribute("iat", new Date()); // Issued at date (now)
		final JwtGenerator<CommonProfile> jwtGenerator = new JwtGenerator<>(
				new SecretSignatureConfiguration(AuthService.JWT_SIGNATURE));
		return jwtGenerator.generate(profile);
	}

	/**
	 * Authorizes a user by the given username and password and returns a
	 * CommonProfile for the user if he could be found.
	 *
	 * @param username
	 *            username of the user to be authorized
	 * @param password
	 *            password for the user to be authorized
	 * @return
	 * @throws UnauthorizedException
	 */
	private CommonProfile authorizeUser(final String username, final String password) throws UnauthorizedException {
		try {
			final User user = this.getUserByUsername(username);
			if (this.matchPassword(password, user.getPassword())) {
				final CommonProfile profile = new CommonProfile();
				profile.setId(user.getId());
				profile.addRoles(user.getRoles());
				profile.addAttribute("username", user.getUsername());
				profile.addAttribute("email", user.getEmail());
				return profile;
			} else {
				throw new UnauthorizedException("Invalid password.");
			}
		} catch (final UnauthorizedException e) {
			throw e;
		} catch (final NotFoundException e) {
			throw new UnauthorizedException("Unknown username.");
		} catch (final Exception e) {
			// All unexpected exceptions go here
			e.printStackTrace();
			throw new UnauthorizedException("Unauthorized");
		}
	}

	/**
	 * Queries the database for a user with the given username.
	 *
	 * @param username
	 *            username to query for
	 * @return
	 * @throws IOException
	 * @throws NotFoundException
	 */
	private User getUserByUsername(final String username) throws IOException, NotFoundException {
		final DatabaseObject result = this.databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS,
				"username", username);
		if (result == null) {
			throw new NotFoundException("No user found with username: " + username);
		} else {
			return (User) result;
		}
	}

	/**
	 * Checks if a given plain text password matches the encrypted one.
	 *
	 * @param plainTextPassword
	 *            the password in plain text to match
	 * @param encryptedPassword
	 *            the encrypted password to match against
	 * @return
	 */
	private boolean matchPassword(final String plainTextPassword, final String encryptedPassword) {
		try {
			if (PasswordEncoder.matches(plainTextPassword, encryptedPassword)) {
				return true;
			} else {
				return false;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the expiration date for tokens.
	 *
	 * @return The time the token will expire.
	 */
	private static Date getExpirationDate() {
		final int minutesToExpiration = Integer.parseInt(AuthService.EXPIRATION_TIME.replaceAll("[\\D]", ""));
		return new Date(Calendar.getInstance().getTimeInMillis() + (minutesToExpiration * 60000));
	}

}
