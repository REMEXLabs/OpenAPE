package org.openape.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openape.api.DatabaseObject;
import org.openape.api.Messages;
import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Spark;

import java.io.IOException;
import java.util.Optional;

public class ProfileRESTInterface extends SuperRestInterface {

    static void setupProfileRESTInterface() {
        final AuthService authService = new AuthService();
        Spark.before("/profile", authService.protect());
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
            User receivedUser = (User) extractObjectFromRequest(req, User.class);
            String id = createUser(receivedUser);
            return "Done! Your ID is " + id;
        });

    }

    private static String createUser(Object user) throws IOException, IllegalArgumentException {
        final DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        String id;
        try {
            id = databaseconnection.storeData(MongoCollectionTypes.USERS, (DatabaseObject) user);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return id;
    }

}
