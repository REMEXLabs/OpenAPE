package org.openape.api.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.openape.api.UserContextList;
import org.openape.api.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserContextListTest {

    private Logger logger = LoggerFactory.getLogger(UserContextListTest.class);
    @Test
    public void testParsing() throws IOException {
        
        UserContext uc = new UserContext();
        uc.setId("1234567890");
        List<UserContext> contexts = new LinkedList<>();
        contexts.add(uc);
        
        
            UserContextList ucl = new UserContextList(contexts, "http://example.com/");
        
logger.info(ucl.getXML());
    logger.info(ucl.getJson()  );

    final ObjectMapper mapper = new ObjectMapper();
    final Object recievedObject = mapper.readValue(ucl.getJson(), UserContextList.class  );
    
    }
    
        }
