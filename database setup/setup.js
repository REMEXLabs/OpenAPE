var connection = new Mongo(<address>:27017);
var database = conn.getDatabase("openAPE");
database.addUser("openAPE", "<password>");
database.createCollection("environment_contexts");
database.createCollection("equipment_contexts");
database.createCollection("resource_offers");
database.createCollection("resource_requests");
database.createCollection("task_contextss");
database.createCollection("user_contexts");
