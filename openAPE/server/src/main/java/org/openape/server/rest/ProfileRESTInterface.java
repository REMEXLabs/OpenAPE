package org.openape.server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.OpenAPEEndPoints;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.ProfileHandler;
import org.openape.server.requestHandler.UserContextRequestHandler;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Spark;

public class ProfileRESTInterface extends SuperRestInterface {

    static void setupProfileRESTInterface() {
        final AuthService authService = new AuthService();
        Spark.before("/profile", authService.authorize("default"));
        Spark.get("/profile", "app", (req, res) -> {
            final SparkWebContext context = new SparkWebContext(req, res);
            final ProfileManager manager = new ProfileManager(context);
            final Optional<CommonProfile> profile = manager.get(false);
            final ObjectMapper mapper = new ObjectMapper();
            res.type(Messages.getString("UserContextRESTInterface.JsonMimeType"));
            return mapper.writeValueAsString(User.getFromProfile(profile.get()));
        });

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

        Spark.before( OpenAPEEndPoints.MY_ID   , authService.authorize("user"));
        Spark.get(OpenAPEEndPoints.MY_ID, (req, res) -> {
        	return authService.getAuthenticatedUser(req, res).getId(); 
        });
        /*Enables admins to change the role of other users
         * 
         */
        Spark.before( OpenAPEEndPoints.USER_ROLES   , authService.authorize("admin"));
        Spark.put(OpenAPEEndPoints.USER_ROLES, (req,res) ->  {
        	List<String> received = (List<String>) SuperRestInterface.extractObjectFromRequest(req, ArrayList.class);

        
        	String authUserId = req.params(OpenAPEEndPoints.USER_ID);
        	logger.info("userid" + authUserId );
        	User storedUser = null;
        	try{
        	storedUser = ProfileHandler.getUser(authUserId);
        	} catch(IllegalArgumentException e) {
        		e.printStackTrace();
        	}
        			
        			storedUser.setRoles(received );
					ProfileHandler.updateUser(storedUser);
        			
        return OpenAPEEndPoints.USER_ROLES_CHANGED; 
        });
        
    }

    
}
