package org.openape.api.test;

import static org.junit.Assert.assertTrue;

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

    private static Logger logger = LoggerFactory.getLogger(UserContextListTest.class);
    
    @Test public void addingContexts() {
    	List contexts = createListWithUserContexts();
    	UserContextList ucl = new UserContextList(contexts, "http://openape.gpii.eu");
    	assertTrue(contexts.size() == ucl.getTotalContexts() );
    }
    @Test
    public void testParsing() throws Exception {
        List contexts = createListWithUserContexts();
        
        
        
            UserContextList ucl = new UserContextList(contexts, "http://example.com/");
        ucl.addContexts(contexts);
        
logger.info(ucl.getXML());
    logger.info(ucl.getJson()  );

    final ObjectMapper mapper = new ObjectMapper();
    final Object recievedObject = mapper.readValue(ucl.getJson(), UserContextList.class  );
    
    }
	public static List createListWithUserContexts() {
		
		UserContext uc = new UserContext();
        uc.setId("1234567890");
        List<UserContext> contexts = new LinkedList<>();
        contexts.add(uc);        
		return contexts;
	}
    
        }
