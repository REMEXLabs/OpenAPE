package org.openape.server.rest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.json.JSONObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.PasswordEncoder;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;

import spark.Spark;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfileRESTInterface extends SuperRestInterface {

    private static String createUser(final User user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            final String hashedPassword = PasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            id = databaseconnection.storeData(MongoCollectionTypes.USERS, user);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return id;
    }

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
        Spark.post(
                "/users",
                (req, res) -> {
                    try {
                        final User receivedUser = (User) SuperRestInterface
                                .extractObjectFromRequest(req, User.class);
                        final String id = ProfileRESTInterface.createUser(receivedUser);
                        return "Done! Your ID is " + id;
                    } catch (final IOException e) {
                        res.status(409);
                        return "Could not create user: " + e.getMessage();
                    }
                });

        Spark.delete(
                "/users",
                (req, res) -> {
                    final AdminSectionRequestHandler adminsectionRequestHandler = new AdminSectionRequestHandler();

                    adminsectionRequestHandler.removeUser(req.queryParams("id"));
                    System.out.println(req.queryParams("id"));

                    return "";
                });

        // edit users
        Spark.put(
                "/users",
                (req, res) -> {
                    try {
                        final AdminSectionRequestHandler adminsectionRequestHandler = new AdminSectionRequestHandler();

                        final JSONObject jsonObj = new JSONObject(req.body().toString());

                        adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "username",
                                jsonObj.getString("username"));
                        adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "email",
                                jsonObj.getString("email"));

                        for (final Object entry : jsonObj.getJSONArray("roles")) {
                            adminsectionRequestHandler.updateUser(jsonObj.getString("id"), "roles",
                                    entry.toString());
                        }

                        // adminsectionRequestHandler.updateUser(jsonObj.getString("id"),
                        // "roles", jsonObj.getJSONArray("roles"));

                        return "user updated";
                    } catch (final Exception err) {
                        return "Could not create user: " + err.getMessage();
                    }
                });

    }

}
