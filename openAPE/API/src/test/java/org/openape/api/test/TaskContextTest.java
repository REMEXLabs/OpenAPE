package org.openape.api.test;

import java.io.IOException;

import org.junit.Test;
import org.openape.api.databaseObjectBase.Property;
import org.openape.api.taskcontext.TaskContext;

public class TaskContextTest {

	@Test
	public void testParsing() {
		
		String json = "{\"task-context\": [[\"http://openape.gpii.eu/test/text\", \"Testtext\"]]}";
		TaskContext.getObjectFromJson(json);
	}
	@Test
	public void testSerilization() throws IOException {
		TaskContext taskContext = new TaskContext();
		Property property = new Property("test", "text");
				taskContext.addProperty(property);
				
				Property booleanProperty = new Property("boolean", true);
				taskContext.addProperty(booleanProperty);
				
				Property floatProperty = new Property("double", 3.141);
				taskContext.addProperty(floatProperty);
				
				Property intProperty = new Property("integer", 2);
				taskContext.addProperty(intProperty);
				
		System.out.println(taskContext.getFrontendJson());
	}
}
