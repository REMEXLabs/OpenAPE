package org.openape.api.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.openape.api.DatabaseObject;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class User extends DatabaseObject {
private static Logger logger = LoggerFactory.getLogger(User.class);

    private String id;
    private String username;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private List<String> roles;

    public static User getFromProfile(CommonProfile profile) {
logger.debug("Profile" + profile);
    	User user = new User();
        user.setId(profile.getId());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setRoles(new ArrayList<>(profile.getRoles()));
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
