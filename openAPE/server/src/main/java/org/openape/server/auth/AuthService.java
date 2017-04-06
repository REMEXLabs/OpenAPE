package org.openape.server.auth;

import org.jboss.resteasy.spi.UnauthorizedException;
import org.openape.api.Messages;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sparkjava.SecurityFilter;

import java.util.Arrays;

public class AuthService {

    private final Config config = new AuthConfigFactory(Messages.getString("Auth.JwtSalt")).build();

    public static String getToken(String username, String password) throws UnauthorizedException {
        if(isValidUser(username, password)) {
            CommonProfile profile = new CommonProfile();
            profile.setId(username);
            profile.addRole("admin");
            return generateJwt(profile);
        } else {
            throw new UnauthorizedException(Messages.getString("Auth.unauthorizedMessage"));
        }
    }

    public SecurityFilter protect() {
        return new SecurityFilter(config, "HeaderClient");
    }

    public SecurityFilter protectWithRole(String role) {
        return new SecurityFilter(config, "HeaderClient", role);
    }

    private static String generateJwt(final CommonProfile profile) {
        JwtGenerator<CommonProfile> jwtGenerator = new JwtGenerator<>(new SecretSignatureConfiguration(Messages.getString("Auth.JwtSalt")));
        return jwtGenerator.generate(profile);
    }

    private static boolean isValidUser(String username, String password) {
        String[] users = {"adam", "ben", "semih"};
        if(Arrays.asList(users).contains(username) && password.equals("mysecret")) {
            return true;
        } else {
            return false;
        }
    }

}
