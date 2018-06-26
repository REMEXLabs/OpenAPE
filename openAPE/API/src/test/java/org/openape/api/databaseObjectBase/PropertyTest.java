package org.openape.api.databaseObjectBase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;

public class PropertyTest {
@Test
public void testConstructors() {
	Property p1 = new Property();
	p1.setName("testProperty");
	p1.setValue("test");
	p1.addDescriptor(new Descriptor() );

}

@Test
public void testEquals() {
	Property p1 = new Property("test", "testString");
Property p2 = new Property("test", "testString");
Assert.assertFalse(p1 == p2);
Assert.assertFalse(p1.equals(null));

Assert.assertEquals(p1,p2);
}

//@Test
public void testHasSameDescriptors() {
	Property p1 = new Property();
	Property p2 = new Property();
	p1.addDescriptor(new Descriptor("testName", "testValue"));
	p2.addDescriptor(new Descriptor("testName", "testValue"));
Assert.assertTrue( p1.hasPropertyTheSameDescriptors(p2));


p1.addDescriptor(new Descriptor("testName2", "testValue"));
p2.addDescriptor(new Descriptor("testName3", "testValue"));
p1.addDescriptor(new Descriptor("testName3", "testValue"));
p2.addDescriptor(new Descriptor("testName2", "testValue"));
Assert.assertTrue( p1.hasPropertyTheSameDescriptors(p2));

p1.addDescriptor(new Descriptor("testName4", "testValue"));
p2.addDescriptor(new Descriptor("testName5", "testValue"));
Assert.assertFalse( p1.hasPropertyTheSameDescriptors(p2));






}
}