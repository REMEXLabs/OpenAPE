package org.openape.api.user;

import java.util.ArrayList;
import java.util.List;

import org.openape.api.DatabaseObject;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

public class User extends DatabaseObject {
	/**
		 * 
		 */
	private static final long serialVersionUID = 5544621526965300349L;

	private static Logger logger = LoggerFactory.getLogger(User.class);

	private String id;
	private String username;
	private String email;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	private List<String> roles;

	public static User getFromProfile(final CommonProfile profile) {
		User.logger.debug("Profile" + profile);
		final User user = new User();
		user.setId(profile.getId());
		user.setUsername(profile.getUsername());
		user.setEmail(profile.getEmail());
		user.setRoles(new ArrayList<>(profile.getRoles()));
		return user;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            Hash of the user's password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public void setRoles(final List<String> roles) {
		this.roles = roles;
	}

}
