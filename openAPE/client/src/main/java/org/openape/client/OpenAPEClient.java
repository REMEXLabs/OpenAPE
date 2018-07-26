package org.openape.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openape.api.ContextList;
import org.openape.api.EnvironmentContextList;
import org.openape.api.EquipmentContextList;
import org.openape.api.OpenAPEEndPoints;
import org.openape.api.PasswordChangeRequest;
import org.openape.api.TaskContextList;
import org.openape.api.UserContextList;
import org.openape.api.auth.TokenResponse;
import org.openape.api.contexts.AbstractContext;
import org.openape.api.contexts.ContextObject;
import org.openape.api.environmentcontext.EnvironmentContext;
import org.openape.api.equipmentcontext.EquipmentContext;
import org.openape.api.listing.Listing;
import org.openape.api.taskcontext.TaskContext;
import org.openape.api.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private String standardMediaType = MediaType.APPLICATION_JSON;
	private final String token;
	private final String userId;

	/**
	 * Standard constructor, sets server adress automatically to
	 * http://openape.gpii.eu/
	 */
	public OpenAPEClient(final String userName, final String password) throws MalformedURLException {
		this(userName, password, "http://openape.gpii.eu");
	}

	public OpenAPEClient(final String userName, final String password, final String uri) throws MalformedURLException {
		// create HTTP client that connects to the server
		OpenAPEClient.logger.info("Initialising OpenAPE client");
		// ClientConfig config = new ClientConfig();
		this.client = ClientBuilder.newClient();// config);
		try {
			URL url = new URL(uri);
		} catch (MalformedURLException e) {
			logger.error("Malformed URL: " + uri + "OpeAPE clint can not beinitialized");
			throw e;
		}
System.out.println("uri: " + uri);


this.webResource = this.client.target(uri);

		// get token for accessing server
		this.token = this.getToken(userName, password);
		OpenAPEClient.logger.info("OpenAPECLIENT received Token for: " + uri);
		OpenAPEClient.logger.info("Token: " + this.token);

		this.userId = this.getMyId();
		logger.debug("userId: " + this.userId);
	}

	public boolean changeUserPassword(final String oldPassword, final String newPassword) {
		final PasswordChangeRequest pwChangeReq = new PasswordChangeRequest(oldPassword, newPassword);
		final Response response = this.getRequest("openape/users/" + this.userId + "/password")
				.put(Entity.entity(pwChangeReq, MediaType.APPLICATION_JSON));

		return this.checkResponse(response);
	}

	public boolean changeUserRoles(final String userId, final List<String> roles) {
		final Response response = this.getRequest("users/" + userId + "/roles")
				.put(Entity.entity(roles, MediaType.APPLICATION_JSON));
		return this.checkResponse(response);
	}

	private boolean checkResponse(final Response response) {
		final int status = response.getStatus();

		if (status != 200) {
			OpenAPEClient.logger.error("Http Status: " + status + "\n" + "Server message: " + response.getStatus());
			logger.error(response.readEntity(String.class) );
			return false;
		}

		OpenAPEClient.logger.debug("Http Status: " + status + "\n Server message: " + response.getStatusInfo());
		return true;

	}

	private URI createContext(final String path, final AbstractContext uploadContext) throws URISyntaxException, IOException {

		final Response response = getRequest(path)
//				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(uploadContext.getFrontendJson()	, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return new URI(response.getHeaderString("Location"));
	}

	public URI createEnvironmentContext(final ContextObject envrionmentContext) throws URISyntaxException, IOException {
		return this.createContext(OpenAPEClient.ENVIRONMENT_CONTEXT_PATH, envrionmentContext);
	}

	public URI createEquipmentContext(final ContextObject equipmentContext) throws URISyntaxException, IOException {
		return this.createContext(OpenAPEClient.EQUIPMENT_CONTEXT_PATH, equipmentContext);
	}

	public Listing createListing(final URI userContextUri, final URI equipmentContextUri, final URI environMentUri,
			final URI taskContextUri) {
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

	public URI createTaskContext(final TaskContext taskContext) throws URISyntaxException, IOException {
		return this.createContext(OpenAPEClient.TASK_CONTEXT_PATH, taskContext);
	}

	public URI createUserContext(final UserContext userContext) throws URISyntaxException, IOException {

		return this.createContext(OpenAPEClient.USER_CONTEXT_PATH, userContext);

	}

	public boolean deleteEquipmentContext(String ctxId) {
		return deleteContext(EQUIPMENT_CONTEXT_PATH, ctxId);
	}
	
	
	public boolean deleteEnvironmentContext(String ctxId) {
		return deleteContext(ENVIRONMENT_CONTEXT_PATH, ctxId);
	}
	
	public boolean deleteTaskContext(String ctxId) {
		return deleteContext(TASK_CONTEXT_PATH, ctxId);
	}
	
	public boolean deleteUserContext(String ctxId) {
		return deleteContext(USER_CONTEXT_PATH, ctxId);
	}
	
	
	private boolean deleteContext(String contextPath, String ctxId) {
		
		Response response = getRequest(contextPath + "/" + ctxId).delete();
		
		return  checkResponse(response);
				
	}

	public boolean updateEnvironmentContext(String ctxId, EnvironmentContext envCtx) throws IOException {
		return updateContext(ENVIRONMENT_CONTEXT_PATH, ctxId, envCtx);
	}
	private boolean updateContext(String contextPath, String ctxId, AbstractContext ctx) throws IOException {
		Response response = getRequest(contextPath + "/" + ctxId)
				.put(Entity.entity(ctx.getFrontendJson()	 , MediaType.APPLICATION_JSON));
		return checkResponse(response);
	}

	public boolean updateEquipmentContext(String ctxId, EquipmentContext ctx) throws IOException {
		return updateContext(EQUIPMENT_CONTEXT_PATH, ctxId, ctx);
	}
	
	public boolean updateTaskContext(String ctxId, TaskContext ctx) throws IOException {
		return updateContext(TASK_CONTEXT_PATH, ctxId, ctx);
	}
	
	public boolean updateUserContext(String ctxId, UserContext ctx) throws IOException {
		return updateContext(USER_CONTEXT_PATH, ctxId, ctx);
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

	Builder getRequest
	(final String path) {
		OpenAPEClient.logger.debug("Building request for URL: " + path);
		return this.webResource.path(path).request().header("Authorization", this.token).accept(standardMediaType);

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
logger.info("luxy: IOFehler");
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
		checkResponse(response);
		String responseString = response.readEntity(String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = mapper.readValue(responseString, TokenResponse.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final String output = tokenResponse.getAccessToken();

		return output;

	}

	public EnvironmentContext getEnvironmentContext(final String environmentContextId) {
		return EnvironmentContext.getObjectFromJson(getContext(ENVIRONMENT_CONTEXT_PATH, environmentContextId));
	}
	
	public EquipmentContext getEquipmentContext(final String equipmentContextId) {
		return EquipmentContext.getObjectFromJson(getContext(EQUIPMENT_CONTEXT_PATH, equipmentContextId));
	}
	
	
	
	public UserContext getUserContext(final String userContextId) {
		return UserContext.getObjectFromJson(getContext(USER_CONTEXT_PATH, userContextId));
	}
	
	public TaskContext getTaskContext(String taskContextId) {
		return 		TaskContext.getObjectFromJson(getContext(TASK_CONTEXT_PATH, taskContextId));
		
	}

	private String getContext(String contextRestEndpoint, String contextId) {
Response response = getRequest(contextRestEndpoint + "/" + contextId).get();		
		checkResponse(response);
		System.out.println("lusm: " + response.getStatus());
		System.out.println(response.getMediaType());
		
		String body =  response.readEntity(String.class);
		
	return body;
	}

	public UserContextList getAllAccessibleUserContexts() {
		return (UserContextList)getContextlist(USER_CONTEXT_PATH, null , UserContextList.class);
	}

	public UserContextList getMyUserContextList() {
		return (UserContextList)getMyContextList(USER_CONTEXT_PATH,  UserContextList.class);
	}

	public UserContextList getOverallAccessibleUserContextList() {
		return (UserContextList)getContextlist(USER_CONTEXT_PATH, null , UserContextList.class);
	}

	public EquipmentContextList getMyEquipmentContextList() {
		return (EquipmentContextList)getMyContextList(EQUIPMENT_CONTEXT_PATH,  EquipmentContextList.class);
	}

	public EnvironmentContextList getMyEnvironmentContextList() {
		return (EnvironmentContextList)getMyContextList(ENVIRONMENT_CONTEXT_PATH,  EnvironmentContextList.class);
	}

	public TaskContextList getMyTaskrContextList() {
		return (TaskContextList)getMyContextList(TASK_CONTEXT_PATH,  TaskContextList.class);
	}

	
	
	private ContextList getMyContextList(String path, Class cl) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("owner", userId );
		return getContextlist(path,filters  , cl);
	}

	
	
	private UserContextList getContextlist(String contextPath, Map<String,String> filters, Class<UserContextList> class1) {
		String filterString = "";
		if (filters != null) {
			
			StringBuilder sb = new StringBuilder();
		sb.append("?");
		for (String key: filters.keySet() ) {
			
			sb.append( key + "=" + filters.get(key) + "&");
			
		}
		
		sb.deleteCharAt(sb.length()-1);
		filterString = sb.toString();
		}
		
		Response response = getRequest(contextPath + filterString).get();
		checkResponse(response);
		
		
		return response.readEntity(class1); 
		
	}
	
}