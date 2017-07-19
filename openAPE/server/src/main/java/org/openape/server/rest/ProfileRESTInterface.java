package org.openape.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.api.usercontext.UserContext;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Spark;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

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
                String id = createUser(receivedUser);
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
       

    }

    private static String createUser(User user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            String hashedPassword = PasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            id = databaseconnection.storeData(MongoCollectionTypes.USERS, user);
           
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return id;
    }
    
    
   

}
