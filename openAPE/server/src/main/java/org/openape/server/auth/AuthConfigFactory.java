package org.openape.server.auth;

import org.pac4j.core.authorization.authorizer.IsAnonymousAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;

/**
 * Created by benjamin on 04.04.17.
 */
public class AuthConfigFactory implements ConfigFactory {

    private final String salt;

    public AuthConfigFactory(final String salt) {
        this.salt = salt;
    }

    @Override
    public Config build() {
        // REST authentication with JWT for a token passed in the url as the token parameter
        final HeaderClient headerClient = new HeaderClient("Authorization", new JwtAuthenticator(new SecretSignatureConfiguration(salt)));
        final AnonymousClient anonymousClient = new AnonymousClient();
        // Define config
        final Clients clients = new Clients(headerClient, anonymousClient);
        final Config config = new Config(clients);
        // Register HttpActionAdapter to respond with proper HTTP response codes on auth errors
        config.setHttpActionAdapter(new HttpActionAdapter());
        // Set Authorizers to check of roles
        config.addAuthorizer("adminOnly", new RequireAnyRoleAuthorizer("admin"));
        config.addAuthorizer("userAndAdmin", new RequireAnyRoleAuthorizer("user", "admin"));
        return config;
    }

}
