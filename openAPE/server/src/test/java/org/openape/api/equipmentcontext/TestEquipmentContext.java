package org.openape.api.equipmentcontext;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openape.api.Descriptor;
import org.openape.api.Property;
import org.openape.api.equipmentcontext.EquipmentContext;

public class TestEquipmentContext {
    public static EquipmentContext sampleEquipmentContext() {
        EquipmentContext equipmentContext = new EquipmentContext();
        Property prop1 = new Property("http://openurc.org/ns/res#friendlyName","My iPad");
        equipmentContext.addProperty(prop1);
        prop1.addDescriptor(new Descriptor("http://www.w3.org/XML/1998/namespace/lang", "en"));
        Property prop2 = new Property("http://openurc.org/ns/res#friendlyName", "Mein iPad");
        equipmentContext.addProperty(prop2);
        prop2.addDescriptor(new Descriptor("http://www.w3.org/XML/1998/namespace/lang", "de"));
        Property prop3 = new Property("http://openurc.org/ns/res#devicePlatform", "iOS");
        equipmentContext.addProperty(prop3);
        Property prop4 = new Property("http://openurc.org/ns/res#deviceType", "iPad-3gen");
        equipmentContext.addProperty(prop4);
        Property prop5 = new Property("http://openurc.org/ns/res#resolution", "1536x2048");
        equipmentContext.addProperty(prop5);
        return equipmentContext;
    }
    
    @Test
    public void testEquals() {
        Assert.assertTrue(sampleEquipmentContext().equals(sampleEquipmentContext()));
    }
    
    @Test
    public void testGetXML() throws IOException {
        EquipmentContext equipmentContext = sampleEquipmentContext();
        String xml = equipmentContext.getXML();
        //System.out.println(xml);
        Assert.assertTrue(equipmentContext.equals(EquipmentContext.getObjectFromXml(xml)));
    }
    

}
