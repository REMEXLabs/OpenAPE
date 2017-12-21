package org.openape.api.group;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestGroupAccessRights {
    private GroupAccessRights sampleGroupAccessRights = new GroupAccessRights();
    private GroupAccessRight sampleAccessRight1 = new GroupAccessRight("groupId", "resourceId", false, false, false, false);
    private GroupAccessRight sampleAccessRight2 = new GroupAccessRight("groupId", "resourceId", true, false, false, false);
    
    @Test
    public void groupAccessRightstest() {
        sampleGroupAccessRights.addGroupAccessRight(sampleAccessRight1);
        sampleGroupAccessRights.addGroupAccessRight(sampleAccessRight2);
        final ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(sampleGroupAccessRights);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
    }
}
