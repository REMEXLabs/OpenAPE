package org.openape.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openape.api.OpenAPEEndPoints;
import org.openape.api.PasswordChangeRequest;
import org.openape.api.auth.TokenResponse;
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
    private final String userId;

    /**
     * Standard constructor, sets server adress automatically to
     * http://openape.gpii.eu/
     */
    public OpenAPEClient(final String userName, final String password) {
        this(userName, password, "http://openape.gpii.eu");
    }

    public OpenAPEClient(final String userName, final String password, final String uri) {
        // create HTTP client that connects to the server
        OpenAPEClient.logger.info("Initialising OpenAPE client");
        // ClientConfig config = new ClientConfig();
        this.client = ClientBuilder.newClient();// config);
        this.webResource = this.client.target(uri);

        // get token for accessing server
        this.token = this.getToken(userName, password);
        OpenAPEClient.logger.info("OpenAPECLIENT received Token for: " + uri);
        OpenAPEClient.logger.info("Token: " + this.token);

        this.userId = this.getMyId();
    }

    public boolean changeUserPassword(final String oldPassword, final String newPassword) {
        final PasswordChangeRequest pwChangeReq = new PasswordChangeRequest(oldPassword,
                newPassword);
        final Response response = this.getRequest("openape/users/" + this.userId + "/password")
                .put(Entity.entity(pwChangeReq, MediaType.APPLICATION_JSON));

        return this.checkResponse(response);
    }

    public boolean changeUserRoles(final String userId, final List<String> roles) {
        final Response response = this.getRequest("users/" + userId + "/roles").put(
                Entity.entity(roles, MediaType.APPLICATION_JSON));
        return this.checkResponse(response);
    }

    private boolean checkResponse(final Response response) {
        final int status = response.getStatus();

        if (status != 200) {
            OpenAPEClient.logger.error("Http Status: " + status + "\n" + "Server message: "
                    + response.getStatus());
            return false;
        }

        OpenAPEClient.logger.debug("Http Status: " + status + "\n Server message: "
                + response.getStatusInfo());
        return true;

    }

    private URI createContext(final String path, final Object uploadContext)
            throws URISyntaxException {

        final Response response = this.webResource.path(path)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(uploadContext, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

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
         * APPLICATION_JSON_TYPE)
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

    private String getMyId() {

        final Response response = this.getRequest(OpenAPEEndPoints.MY_ID).get();
        this.checkResponse(response);
        final String id = response.readEntity(String.class);
        OpenAPEClient.logger.debug("Received user id: " + id);
        return id;
    }

    Builder getRequest(final String path) {
        OpenAPEClient.logger.debug("Building request for URL: " + path);
        return this.webResource.path(path).request().header("Authorization", this.token);

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

        final String output = response.readEntity(TokenResponse.class).getAccessToken();

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
