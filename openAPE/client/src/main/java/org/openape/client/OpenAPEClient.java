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
/** @author Lukas Smirek
*/

public class OpenAPEClient {
	private static Logger logger = LoggerFactory.getLogger(OpenAPEClient.class);
private Client client;
private WebTarget webResource;
private String token;
private String userId;
static final String ENVIRONMENT_CONTEXT_PATH = "api/environment-contexts";
static final String EQUIPMENT_CONTEXT_PATH = "api/equipment-contexts";
static final String TASK_CONTEXT_PATH = "api/task-contexts";
static final String USER_CONTEXT_PATH = "api/user-contexts";
static final String LISTING_PATH = "api/listings";
/** Standard constructor, sets server adress automatically to http://openape.gpii.eu/ */
public OpenAPEClient(String userName, String password){
this(userName, password, "http://openape.gpii.eu");
}

public OpenAPEClient(String userName, String password, String uri) {
//	create HTTP client that connects to the server
	logger.info("Initialising OpenAPE client");
//	ClientConfig config = new ClientConfig();
	 client = ClientBuilder.newClient();//config);
webResource = client.target(uri);

//get token for accessing server
this.token = getToken(userName,password);
logger.info("OpenAPECLIENT received Token for: " + uri);
logger.info("Token: " + token);

this.userId = getMyId();
}

private String getMyId() {
	
	Response response = getRequest(OpenAPEEndPoints.MY_ID  ).get();
checkResponse(response);
	String id = response.readEntity(String.class);
	logger.debug("Received user id: " + id);
	return id; 
}

private String getToken(String userName,String password){
	
	Form form = new Form();
	form.param("grant_type","password");
	form.param( "username",userName);
	form.param("password",password);
	
	Response response = webResource.path("token")
			.request()
			.post(Entity.form(form));
	
	int status = response.getStatus();
	logger.debug("Response code: " + status);
			if (status != 200){
				logger.error("Failed : HTTP error code : " + status  +".\n Server message: " + response.readEntity(String.class)     );
				throw new RuntimeException("Failed : HTTP error code : " + status );
			}
			
		    String output = response.readEntity(TokenResponse.class).getAccessToken()	;
		 
return output;

					}

public URI createUserContext(UserContext userContext) throws URISyntaxException{
	
	return createContext(USER_CONTEXT_PATH, userContext);
			
}

public URI createEquipmentContext(EquipmentContext equipmentContext) throws URISyntaxException{
	return createContext(EQUIPMENT_CONTEXT_PATH , equipmentContext);
}

public URI createEnvironmentContext(EnvironmentContext envrionmentContext) throws URISyntaxException{
	return createContext(ENVIRONMENT_CONTEXT_PATH, EnvironmentContext.class);
}

public URI createTaskContext(TaskContext taskContext) throws URISyntaxException{
	return createContext(TASK_CONTEXT_PATH, taskContext);
}

public Listing createListing(URI userContextUri, URI equipmentContextUri, URI environMentUri, URI taskContextUri){
	/*
	Response response = webResource.path(LISTING_PATH).request(MediaType.APPLICATION_JSON_TYPE)
		    .post(Entity.entity(listingQuery,MediaType.APPLICATION_JSON));
		    		
			if (response.getStatus() != 201){
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			
		    Listing output = response.readEntity(Listing.class);
		 	return output;
		 	*/
	return null;
}





private URI createContext(String path,Object uploadContext ) throws URISyntaxException{
	
	Response response = webResource.path(path).request(MediaType.APPLICATION_JSON_TYPE)
    .post(Entity.entity(uploadContext,MediaType.APPLICATION_JSON));
    		
	if (response.getStatus() != 201){
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}
	
    
 
return new URI(response.getHeaderString("Location"));
}


	public void getListing(String url) {
		Invocation.Builder invocationBuilder = webResource.request();


			 
				

	}
	
	public UserContext getUserContext(String userContextId){
        Invocation.Builder invocationBuilder = webResource.path(USER_CONTEXT_PATH + userContextId).request();
invocationBuilder.header("Authorization",this.token );
        
Response response = invocationBuilder.get();

			if (response.getStatus() != 200) {
UserContext userContext = 		 response.readEntity(UserContext.class);
return userContext;
	}
			return null;
}
	
	
	public File getResource(URI uri, String targetFile){

        Invocation.Builder invocationBuilder = webResource.path(uri.getPath()).request();

Response response = invocationBuilder.get();

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			
			InputStream in = (InputStream) response.readEntity(InputStream.class);
			
			File tf = new File(targetFile);
					try {
						Files.copy(in, tf.toPath());
		                 in.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}


		
		return tf;
	}

	public File getResource(String url, String targetFile) throws URISyntaxException {
		URI uri = new URI(url);
return 		getResource(uri, targetFile);
	}

	public boolean changeUserPassword(String oldPassword, String newPassword) {
		PasswordChangeRequest pwChangeReq = new PasswordChangeRequest(oldPassword, newPassword);
		Response response = getRequest("openape/users/" + userId + "/password").put(Entity.entity(pwChangeReq, MediaType.APPLICATION_JSON));
		
		return checkResponse(response);
			}
	
	public boolean changeUserRoles(String userId, List<String> roles) {
		Response response = getRequest("users/"+ userId + "/roles").put(Entity.entity(roles, MediaType.APPLICATION_JSON));
		return checkResponse(response);
	}
	
	private boolean checkResponse(Response response) {
		int status = response.getStatus();
		
		if (status != 200) {
			logger.error("Http Status: " + status + "\n" + "Server message: " + response.getStatus() );
					return false;
					}
		
		logger.debug("Http Status: " + status + "\n Server message: " + response.getStatusInfo() );
		return true;

		
	}

	Builder getRequest(String path){
logger.debug("Building request for URL: " + path);
		return webResource.path(path).request().header("Authorization", this.token);
				
	}
}
