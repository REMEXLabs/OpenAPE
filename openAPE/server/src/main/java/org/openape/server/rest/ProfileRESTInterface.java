package org.openape.server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.openape.api.DatabaseObject;
import org.openape.api.OpenAPEEndPoints;
import org.openape.api.PasswordChangeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.requestHandler.ProfileHandler;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;


public class ProfileRESTInterface extends SuperRestInterface {

    static void setupProfileRESTInterface() {
        final AuthService authService = new AuthService();
        Spark.before("/profile", authService.authorize("default"));
        /* returns a user object /*
        /*Spark.get("/profile", "app", (req, res) -> {
            final SparkWebContext context = new SparkWebContext(req, res);
            final ProfileManager manager = new ProfileManager(context);
            final Optional<CommonProfile> profile = manager.get(false);
            final ObjectMapper mapper = new ObjectMapper();
            res.type(Messages.getString("UserContextRESTInterface.JsonMimeType"));
            return mapper.writeValueAsString(User.getFromProfile(profile.get()));
        });
*/
        // TODO: Remove this before live deployment!
        Spark.post("/users", (req, res) -> {
            try {
                User receivedUser = (User) extractObjectFromRequest(req, User.class);
                String id = ProfileHandler.createUser(receivedUser);
                return "Done! Your ID is " + id;
            } catch(IOException e) {
                res.status(409);
                return "Could not create user: " + e.getMessage();
            }
        });
        
        Spark.delete("/users", (req, res) -> {
        	AdminSectionRequestHandler adminsectionRequestHandler = new AdminSectionRequestHandler();
        	
        	adminsectionRequestHandler.removeUser(req.queryParams("id"));
        	System.out.println(req.queryParams("id"));

            return "";
        });
        
        //edit users
        Spark.put("/users", (req, res) -> {
            try {
            	AdminSectionRequestHandler adminsectionRequestHandler = new AdminSectionRequestHandler();
            	
            	
            	JSONObject jsonObj = new JSONObject(req.body().toString());
    
            	adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "username", jsonObj.getString("username"));
            	adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "email", jsonObj.getString("email"));
            	
            	for(Object entry: jsonObj.getJSONArray("roles")){
            		adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "roles", entry.toString());
            	}
            	
            	//adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "roles", jsonObj.getJSONArray("roles"));
            	
                return "user updated";
            } catch(Exception err) {
                return "Could not create user: " + err.getMessage();
            }
        });
       

        Spark.before( OpenAPEEndPoints.MY_ID   , authService.authorize("user"));
        Spark.get(OpenAPEEndPoints.MY_ID, (req, res) -> {
        	logger.info("blablubber");
        	String id = authService.getAuthenticatedUser(req, res).getId();
        	logger.info("id: " + id);
        	return id;  
        });
        
        Spark.before( OpenAPEEndPoints.USER_PASSWORD   , authService.authorize("user"));
        Spark.put(OpenAPEEndPoints.USER_PASSWORD, (req,res) ->  {
        logger.info("blabla");
        	User authUser = authService.getAuthenticatedUser(req, res);
        	
        	PasswordChangeRequest pwChangeReq = null; 
        try{
        	pwChangeReq  = (PasswordChangeRequest) SuperRestInterface.extractObjectFromRequest(req, PasswordChangeRequest.class	);
        } catch (Exception e){
        	res.status(400);
        	return "Invalide Password chagne  request";
        }
        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();		
        final DatabaseObject result = databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS, "username", authUser.getUsername() );
        if(result == null) {
            throw new NotFoundException("No user found with username: " + authUser.getUsername());
            
        } 
User user = (User) result;

<<<<<<< HEAD
if(PasswordEncoder.matches(pwChangeReq.oldPassword, user.getPassword())) {
        		
        	user.setPassword(      	PasswordEncoder.encode(pwChangeReq.newPassword));
        	ProfileHandler.updateUser(user);
        	
            logger.debug("PW successfuly changed");
        	return "success";        
        } else {
        	logger.debug("Changing password failed");
        	res.status(403);
        	return "forbidden";
        }
        
        });
        
        /*Enables admins to change the role of other users
         * 
         */
        Spark.before( OpenAPEEndPoints.USER_ROLES   , authService.authorize("admin"));
        Spark.put(OpenAPEEndPoints.USER_ROLES, (req,res) ->  {
        	List<String>  receivedRoles = null; 
        	try{
        	receivedRoles = (List<String>) SuperRestInterface.extractObjectFromRequest(req, ArrayList.class);
        	} catch (Exception e) {
        		res.status(400);
        		return "Invalide JSON format for user roles object"; 
        	}
        
        	String requestedUserId = req.params(OpenAPEEndPoints.USER_ID);
        	
        	User storedUser = null;
        	try{
        	storedUser = ProfileHandler.getUser(requestedUserId);
        	} catch(IllegalArgumentException e) {
        		res.status(404);
        		return OpenAPEEndPoints.userDoesNotExist(requestedUserId );
        	}
        			
        			storedUser.setRoles(receivedRoles );
					ProfileHandler.updateUser(storedUser);
        			return OpenAPEEndPoints.USER_ROLES_CHANGED;	
        					
        });
        
    }
}
