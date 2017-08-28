package org.openape.api.user;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUser {
    @Test
    public void jsonTest() throws JsonProcessingException {
        final User user = new User();
        user.setEmail("email");
        user.setId("id");
        user.setPassword("password");
        user.setUsername("username");

        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(user);
        System.out.println(json);
    }
}
