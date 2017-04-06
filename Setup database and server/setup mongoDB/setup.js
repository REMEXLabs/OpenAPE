var connection = new Mongo("localhost:27017");
var database = connection.getDB("openAPE");
database.createUser({
     user: "openAPE",
     pwd: "<userPassword>",
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
