package org.openape.server.api.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.user.User;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Test {

    public static void main(final String[] args) throws IOException {

        final MongoCredential credential = MongoCredential.createCredential("openAPE", "openAPE",
                "1234".toCharArray());
        final MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017),
                Arrays.asList(credential));

        // MongoClientURI uri = new
        // MongoClientURI("mongodb://openAPE:1234@localhost/?authSource=openAPE");
        // MongoClient mongoClient = new MongoClient(uri);

        // MongoClient mongoClient = new MongoClient("localhost", 27017);
        final MongoDatabase database = mongoClient.getDatabase("openAPE");

        final BasicDBObject filter = new BasicDBObject();
        // BasicDBObject filter2 = new BasicDBObject();
        // filter2.put("userId", "5988d525ebc8292a84dfff64");
        // filter.put("members", filter2);
        // filter.put("members.userId", "12");

        final BasicDBObject query = new BasicDBObject();
        query.put("members.userId", "12");
        query.put("members.state", "ADMIN");
        // query.put("members.state", new Document("$in",
        // Arrays.asList("MEMBER", "ADMIN")));

        final BasicDBList and = new BasicDBList();
        and.add(new BasicDBObject("members.userId", "12"));
        and.add(new BasicDBObject("members.state", "ADMIN"));
        final BasicDBObject query2 = new BasicDBObject("$and", and);

        /*
         * BasicDBObject query3 = new BasicDBObject(); BasicDBObject members =
         * new BasicDBObject(); BasicDBList elemMatch = new BasicDBList();
         * BasicDBObject memberCondition1 = new BasicDBObject("userId", "12");
         * BasicDBObject memberCondition2 = new BasicDBObject("state", new
         * Document("$in", Arrays.asList("MEMBER", "ADMIN")));
         * elemMatch.add(memberCondition1); elemMatch.add(memberCondition2);
         * members.put("$elemMatch", elemMatch);
         */

        final BasicDBObject elemMatch = new BasicDBObject();
        elemMatch.put("userId", "12");
        elemMatch.put("state", new Document("$in", Arrays.asList("MEMBER", "ADMIN")));
        final BasicDBObject members = new BasicDBObject();
        members.put("$elemMatch", elemMatch);
        final BasicDBObject query4 = new BasicDBObject();
        query4.put("members", members);

        final FindIterable<Document> groupsCollection = database.getCollection("groups")
                .find(query);
        // FindIterable<Document> groupsCollection =
        // database.getCollection("groups").find(Document.parse("{\"members.userId\": \"12\"}"));
        final MongoCursor<Document> iterator = groupsCollection.iterator();
        System.out.println("before while loop");
        while (iterator.hasNext()) {
            final Document document = iterator.next();
            System.out.println("group = " + document.toJson());
        }
        iterator.close();

        mongoClient.close();

        final User user1 = new User();
        user1.setEmail("tobias.ableitner@t-online.de");
        user1.setUsername("user1");
        user1.setPassword("123");
        final List<String> roles1 = new ArrayList<>();
        roles1.add("admin");
        user1.setRoles(roles1);

        final List<GroupMember> members1 = new ArrayList<>();
        members1.add(new GroupMember("5988cd85ebc8292bc07e953f", GroupMembershipStatus.MEMBER));
        members1.add(new GroupMember("12", GroupMembershipStatus.MEMBER));
        final Group group1 = new Group("group1", members1);

        final List<GroupMember> members2 = new ArrayList<>();
        members2.add(new GroupMember("5988cd85ebc8292bc07e953f", GroupMembershipStatus.MEMBER));
        final Group group2 = new Group("group2", members2);

        final List<GroupMember> members3 = new ArrayList<>();
        members3.add(new GroupMember("12", GroupMembershipStatus.ADMIN));
        final Group group3 = new Group("group3", members3);

        final List<GroupMember> members4 = new ArrayList<>();
        members4.add(new GroupMember("5988cd85ebc8292bc07e953f", GroupMembershipStatus.ADMIN));
        members4.add(new GroupMember("12", GroupMembershipStatus.MEMBER));
        final Group group4 = new Group("group4", members4);

        final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
       /* final List groups = databaseConnection.getDatabaseObjectsByQuery(MongoCollectionTypes.GROUPS,
                query4, true);
        for (final Object object : groups) {
            final Group group = (Group) object;
            System.out.println("groupName = " + group.getName());
        }*/
        
        // databaseConnection.storeData(MongoCollectionTypes.USERS, user1);
        // databaseConnection.storeData(MongoCollectionTypes.GROUPS, group4);
        // List<Document> documents =
        // databaseConnection.getAllDocuments(MongoCollectionTypes.USERS);
        // System.out.println(documents.get(0).getObjectId("_id"));
        // User user =
        // (User)databaseConnection.getByUniqueAttribute(MongoCollectionTypes.USERS,
        // "username", "user1");
        // System.out.println(user.getId());
    }

}
