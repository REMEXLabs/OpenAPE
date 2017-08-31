package org.openape.api.test;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.openape.api.UserContextList;
import org.openape.api.usercontext.UserContext;

public class UserContextListTest {

    @Test
    public void testParsing() throws JAXBException{
        
        UserContext uc = new UserContext();
        uc.setId("1234567890");
        List<UserContext> contexts = new LinkedList<>();
        contexts.add(uc);
        
        
            UserContextList ucl = new UserContextList(contexts, "http://example.com/");
        
                    
    }
    
    
        }
