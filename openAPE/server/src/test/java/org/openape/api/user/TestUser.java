package org.openape.api.user;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUser {
    @Test
    public void jsonTest() throws JsonProcessingException{
        User user = new User();
        user.setEmail("email");
        user.setId("id");
        user.setPassword("password");
        user.setUsername("username");
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        System.out.println(json);
    }
}
