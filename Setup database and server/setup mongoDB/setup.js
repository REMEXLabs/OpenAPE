var connection = new Mongo("localhost:27017");
var database = connection.getDB("openAPE2");
database.createUser({
     user: "openAPE2",
     pwd: "121212",
     roles: [ "readWrite", "dbAdmin" ]});
database.createCollection("environment_contexts");
database.createCollection("equipment_contexts");
database.createCollection("resource_descriptions");
database.createCollection("listings");
database.createCollection("task_contexts");
database.createCollection("user_contexts");
database.createCollection("resources");
database.createCollection("users");
database.createCollection("test");
database.test.insert( { x: 1 } );

// Create indexes to make username and email unique fields in the users collection
database.getCollection("users").createIndex({ "username": 1 }, { unique: true });
database.getCollection("users").createIndex({ "email": 1 }, { unique: true });