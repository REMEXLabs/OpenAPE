package org.openape.server.auth;

import org.jboss.resteasy.spi.NotFoundException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sparkjava.SecurityFilter;

import java.io.IOException;

/**
 * Authorize users and generate tokens.
 */

public class AuthService {

    private final Config config = new AuthConfigFactory(Messages.getString("Auth.JwtSalt")).build();
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    /**
     * Tries to authorize using the given credentials (username and password) and returns a JWT String for the user
     * if the authorization was successful.
     *
     * @param username username to use for the authorization
     * @param password password to use for the authorization
     * @return Token String
     * @throws UnauthorizedException if user can not be authorized
     */
    public String getToken(String username, String password) throws UnauthorizedException {
        CommonProfile profile = authorizeUser(username, password);
        return generateJwt(profile);
    }

    public SecurityFilter protect() {
        return new SecurityFilter(config, "HeaderClient");
    }

    public SecurityFilter protectWithRole(String role) {
        return new SecurityFilter(config, "HeaderClient", role);
    }

    /**
     * Generates a Json Web Token (JWT) for the given CommonProfile.
     *
     * @param profile the profile the token should be created for
     * @return a JWT as String
     */
    private static String generateJwt(final CommonProfile profile) {
        JwtGenerator<CommonProfile> jwtGenerator = new JwtGenerator<>(new SecretSignatureConfiguration(Messages.getString("Auth.JwtSalt")));
        return jwtGenerator.generate(profile);
    }

    /**
     * Authorizes a user by the given username and password and returns a CommonProfile for the user if he could be
     * found.
     *
     * @param username username of the user to be authorized
     * @param password password for the user to be authorized
     * @return
     * @throws UnauthorizedException
     */
    private CommonProfile authorizeUser(String username, String password) throws UnauthorizedException {
        try {
            User user = getUserByUsername(username);
            if (matchPassword(password, user.getPassword())) {
                CommonProfile profile = new CommonProfile();
                profile.setId(user.getUsername());
                profile.addRoles(user.getRoles());
                profile.addAttribute("username", user.getUsername());
                profile.addAttribute("email", user.getEmail());
                return profile;
            } else {
                throw new UnauthorizedException("Invalid password.");
            }
        } catch(UnauthorizedException e) {
            throw e;
        } catch(NotFoundException e) {
            throw new UnauthorizedException("Unknown username.");
        } catch(Exception e) {
            // All unexpected exceptions go here
            e.printStackTrace();
            throw new UnauthorizedException("Unauthorized");
        }
    }

    /**
     * Queries the database for a user with the given username.
     *
     * @param username username to query for
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    private User getUserByUsername(String username) throws IOException, NotFoundException {
        final DatabaseObject result = databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS, "username", username);
        if(result == null) {
            throw new NotFoundException("No user found with username: " + username);
        } else {
            return (User) result;
        }
    }

    /**
     * Checks if a given plain text password matches the encrypted one.
     *
     * @param plainTextPassword the password in plain text to match
     * @param encryptedPassword the encrypted password to match against
     * @return
     */
    private boolean matchPassword(String plainTextPassword, String encryptedPassword) {
        try {
            if(passwordEncoder.matches(plainTextPassword, encryptedPassword)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
