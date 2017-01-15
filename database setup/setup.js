var connection = new Mongo("<serverAddress>:27017");
var database = connection.getDB("openAPE");
database.createUser({
     user: "openAPE",
     pwd: "<userPassword>",
     roles: [ "readWrite", "dbAdmin" ]});
database.createCollection("environment_contexts");
database.createCollection("equipment_contexts");
database.createCollection("resource_description");
database.createCollection("listing");
database.createCollection("task_contexts");
database.createCollection("user_contexts");
database.createCollection("test");
database.test.insert( { x: 1 } );
