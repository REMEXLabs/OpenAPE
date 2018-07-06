package org.openape.api.equipmentcontext;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openape.api.contexts.ContextObject;
import org.openape.api.databaseObjectBase.Descriptor;
import org.openape.api.databaseObjectBase.Property;

public class TestEquipmentContext {
    public static ContextObject createSampleEquipmentContext() {
        final ContextObject equipmentContext = new EquipmentContext();
        final Property prop1 = new Property("http://openurc.org/ns/res#friendlyName", "My iPad");
        equipmentContext.addProperty(prop1);
        prop1.addDescriptor(new Descriptor("http://www.w3.org/XML/1998/namespace/lang", "en"));
        final Property prop2 = new Property("http://openurc.org/ns/res#friendlyName", "Mein iPad");
        equipmentContext.addProperty(prop2);
        prop2.addDescriptor(new Descriptor("http://www.w3.org/XML/1998/namespace/lang", "de"));
        final Property prop3 = new Property("http://openurc.org/ns/res#devicePlatform", "iOS");
        equipmentContext.addProperty(prop3);
        final Property prop4 = new Property("http://openurc.org/ns/res#deviceType", "iPad-3gen");
        equipmentContext.addProperty(prop4);
        final Property prop5 = new Property("http://openurc.org/ns/res#resolution", "1536x2048");
        equipmentContext.addProperty(prop5);
        return equipmentContext;
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(
                TestEquipmentContext.createSampleEquipmentContext().equalContext(TestEquipmentContext.createSampleEquipmentContext()));
    }

    @Test
    public void testGetJson() throws IOException {
        final ContextObject sample = TestEquipmentContext.createSampleEquipmentContext();
        final String json = sample.getFrontendJson();
        System.out.println(json);
        Assert.assertTrue(sample.equalContext(EquipmentContext.getObjectFromJson(json)));
    }

    @Test
    public void testGetXML() throws IOException {

    	final ContextObject equipmentContext = TestEquipmentContext.createSampleEquipmentContext();
        final String xml = equipmentContext.getXML();
     System.out.println(xml);   
        EquipmentContext compare = EquipmentContext.getObjectFromXml(xml);
        System.out.println("Hier soll's losgehen:");
        Assert.assertTrue(equipmentContext.equalContext(compare) );
    }

    @Test
    public void testSimpleEqContext() throws IOException {
    	ContextObject context = new EquipmentContext();
    	Property property = new Property("test", "test");
    	context.addProperty(property);
    	String xml= context.getXML();
    	System.out.println("Simple: " + xml );
    	ContextObject contextFromXml = EquipmentContext.getObjectFromXml(xml);
    }
}
