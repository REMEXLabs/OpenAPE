package org.openape.server.rest;

import org.jboss.resteasy.spi.UnauthorizedException;
import org.openape.server.auth.AuthService;
import spark.Spark;

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
            try {
                // Try to get a token for the given username and password
                return authService.getToken(username, password);
            } catch(UnauthorizedException e) {
                // If user could not get authorized, return status 401 (Unauthorized)
                res.status(HTTP_STATUS_UNAUTHORIZED);
                return e.getMessage();
            }
        });
    }

}
