package org.openape.server.rest;

import org.openape.server.auth.AuthService;
import org.openape.server.auth.UnauthorizedException;
import spark.Spark;
import spark.utils.StringUtils;

/**
 * Rest Endpoint to get a token in exchange for a username and password.
 */

class TokenRESTInterface extends SuperRestInterface {

    /**
     * Register a new route POST /token to get a token in exchange for a username and password.
     */

    static void setupTokenRESTInterface(AuthService authService) {
        Spark.post("/token", (req, res) -> {
            final String username = req.queryParams("username");
            final String password = req.queryParams("password");
            final String grantType = req.queryParams("grant_type");

            // Return 400 Bad Request if either username or password are empty (see RFC 6749 section 5.2)
            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                res.status(HTTP_STATUS_BAD_REQUEST);
                res.type("application/json");
                return AuthService.buildAuthErrorJSON("invalid_grant", "Username and password are required");
            }

            // Return 400 Bad Request if the grant_type parameter is not set to password
            if(StringUtils.isEmpty(grantType) || !grantType.equals("password")) {
                res.status(HTTP_STATUS_BAD_REQUEST);
                res.type("application/json");
                return AuthService.buildAuthErrorJSON("invalid_grant", "Grant type has to be set to `password`");
            }

            // Create an access token from the given username and password
            try {
                res.type("application/json");
                return authService.getJSONToken(username, password);
            } catch(UnauthorizedException e) {
                // If user could not get authorized, return Bad Request
                res.status(HTTP_STATUS_BAD_REQUEST);
                return AuthService.buildAuthErrorJSON("invalid_grant", "Invalid or expired credentials");
            } catch(Exception e) {
                // Catch any unexpected exceptions with status code 401 Unauthorized
                res.status(HTTP_STATUS_UNAUTHORIZED);
                return "Authorization failed";
            }
        });
    }

}
