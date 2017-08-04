package org.openape.server.rest;

import java.io.IOException;
import java.util.List;

import org.openape.api.OpenAPEEndPoints;
import org.openape.api.groups.GroupListElement;
import org.openape.api.groups.GroupMembershipRequest;
import org.openape.api.groups.GroupRequest;
import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.requestHandler.GroupManagementHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Spark;

public class GroupManagementRestInterface extends SuperRestInterface{

	public static void setupGroupManagementRestInterface(GroupManagementHandler groupManagementHandler,
			AuthService authService) {
		
		Spark.get(OpenAPEEndPoints.GROUPS,(req,res) -> {
			List<GroupListElement> result = null;
			final ObjectMapper mapper = new ObjectMapper();
            final String jsonData = mapper.writeValueAsString(result);
            return jsonData;
            		});
		
		Spark.post(OpenAPEEndPoints.GROUPS,(req,res) -> {
			try{
			GroupRequest groupRequest = (GroupRequest) SuperRestInterface.extractObjectFromRequest(req, GroupRequest.class);
		
		User authUser = authService.getAuthenticatedUser(req, res);
		
		
String groupId;
		groupId = GroupManagementHandler.createGroup(groupRequest.getGroupname(), groupRequest.getDescription(), groupRequest.getEntryRequirements(), authUser.getId() );
		res.status(SuperRestInterface.HTTP_STATUS_CREATED);
		return groupId;
			}catch (JsonParseException | JsonMappingException e) {
            // If the parse is not successful return bad request
            // error code.
            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
            return e.getMessage();
        } catch (final IOException e) {
            res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
   
	});
		Spark.put(OpenAPEEndPoints.GROUP_MEMBER, (req, res) -> {
			GroupMembershipRequest gmsr = SuperRestInterface.extractFromRequest(GroupMembershipRequest.class , req);
			
			return "Success";
		});
}
}
