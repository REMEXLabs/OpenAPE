package org.openape.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.listing.Listing;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lukas Smirek
 */

public class OpenAPEClient {
    private static Logger logger = LoggerFactory.getLogger(OpenAPEClient.class);
    static final String ENVIRONMENT_CONTEXT_PATH = "api/environment-contexts";
    static final String EQUIPMENT_CONTEXT_PATH = "api/equipment-contexts";
    static final String TASK_CONTEXT_PATH = "api/task-contexts";

    static final String USER_CONTEXT_PATH = "api/user-contexts";
    static final String LISTING_PATH = "api/listings";
    private final Client client;
    private final WebTarget webResource;
    private final String token;

    /**
     * Standard constructor, sets server adress automatically to
     * http://openape.gpii.eu/
     */
    public OpenAPEClient(final String userName, final String password) {
        this(userName, password, "http://openape.gpii.eu");
    }

    public OpenAPEClient(final String userName, final String password, final String uri) {
        // create HTTP client that connects to the server
        System.out.println("Constructor");
        // ClientConfig config = new ClientConfig();
        this.client = ClientBuilder.newClient();// config);
        this.webResource = this.client.target(uri);

        // get token for accessing server
        this.token = this.getToken(userName, password);
        OpenAPEClient.logger.info("OpenAPECLIENT received Token for: " + uri);
    }

    private URI createContext(final String path, final Object uploadContext)
            throws URISyntaxException {

        final Response response = this.webResource.path(path)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(uploadContext, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        final String output = response.readEntity(String.class);

        return new URI(response.getHeaderString("Location"));
    }

    public URI createEnvironmentContext(final EnvironmentContext envrionmentContext)
            throws URISyntaxException {
        return this.createContext(OpenAPEClient.ENVIRONMENT_CONTEXT_PATH, EnvironmentContext.class);
    }

    public URI createEquipmentContext(final EquipmentContext equipmentContext)
            throws URISyntaxException {
        return this.createContext(OpenAPEClient.EQUIPMENT_CONTEXT_PATH, equipmentContext);
    }

    public Listing createListing(final URI userContextUri, final URI equipmentContextUri,
            final URI environMentUri, final URI taskContextUri) {
        /*
         * Response response = webResource.path(LISTING_PATH).request(MediaType.
         * APPLICATION_JSON_TYPE )
         * .post(Entity.entity(listingQuery,MediaType.APPLICATION_JSON));
         * 
         * if (response.getStatus() != 201){ throw new RuntimeException(
         * "Failed : HTTP error code : " + response.getStatus()); }
         * 
         * Listing output = response.readEntity(Listing.class); return output;
         */
        return null;
    }

    public URI createTaskContext(final TaskContext taskContext) throws URISyntaxException {
        return this.createContext(OpenAPEClient.TASK_CONTEXT_PATH, taskContext);
    }

    public URI createUserContext(final UserContext userContext) throws URISyntaxException {

        return this.createContext(OpenAPEClient.USER_CONTEXT_PATH, userContext);

    }

    public void getListing(final String url) {
        final Invocation.Builder invocationBuilder = this.webResource.request();

    }

    public File getResource(final String url, final String targetFile) throws URISyntaxException {
        final URI uri = new URI(url);
        return this.getResource(uri, targetFile);

    }

    public File getResource(final URI uri, final String targetFile) {

        final Invocation.Builder invocationBuilder = this.webResource.path(uri.getPath()).request();

        final Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        final InputStream in = response.readEntity(InputStream.class);

        final File tf = new File(targetFile);
        try {
            Files.copy(in, tf.toPath());
            in.close();

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return tf;
    }

    private String getToken(final String userName, final String password) {
        final String tokenRequest = "grant_type=password&username=" + userName + "&password="
                + password;
        final Form form = new Form();
        form.param("grant_type", "password");
        form.param("username", userName);
        form.param("password", password);

        final Response response = this.webResource.path("token").request().post(Entity.form(form));

        final int status = response.getStatus();
        OpenAPEClient.logger.debug("Response code: " + status);
        if (status != 200) {
            OpenAPEClient.logger.error("Failed : HTTP error code : " + status
                    + ".\n Server message: " + response.readEntity(String.class));
            throw new RuntimeException("Failed : HTTP error code : " + status);
        }

        final String output = response.readEntity(String.class);

        return output;

    }

    public UserContext getUserContext(final String userContextId) {
        final Invocation.Builder invocationBuilder = this.webResource.path(
                OpenAPEClient.USER_CONTEXT_PATH + userContextId).request();
        invocationBuilder.header("Authorization", this.token);

        final Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            final UserContext userContext = response.readEntity(UserContext.class);
            return userContext;
        }
        return null;
    }

}
