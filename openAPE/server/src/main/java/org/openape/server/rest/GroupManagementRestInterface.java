package org.openape.server.rest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openape.api.OpenAPEEndPoints;
import org.openape.api.groups.GroupListElement;
import org.openape.api.groups.GroupMembershipRequest;
import org.openape.api.groups.GroupMembershipStatus;
import org.openape.api.groups.GroupRequest;
import org.openape.api.user.User;
import org.openape.server.api.group.Group;
import org.openape.server.api.group.GroupMember;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.GroupAuthService;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.requestHandler.GroupManagementHandler;
import org.openape.server.requestHandler.ProfileHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Spark;

public class GroupManagementRestInterface extends SuperRestInterface {

    public static void setupGroupManagementRestInterface(
            final GroupManagementHandler groupManagementHandler, final AuthService authService) {

        Spark.get(OpenAPEEndPoints.GROUPS, (req, res) -> {
            final List<GroupListElement> result = null;
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonData = mapper.writeValueAsString(result);
            return jsonData;
        });
/* receive requests for new resource groups
 *start the creation process of the group 
 */
        Spark.post(
                OpenAPEEndPoints.GROUPS,
                (req, res) -> {
                    try {
                        final GroupRequest groupRequest = (GroupRequest) SuperRestInterface
                                .extractObjectFromRequest(req, GroupRequest.class);

                        final User authUser = authService.getAuthenticatedUser(req, res);

                        String groupId;
                        groupId = GroupManagementHandler.createGroup(groupRequest.getGroupname(),
                                groupRequest.getDescription(), groupRequest.getEntryRequirements(),
                                authUser.getId());
                        res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                        return groupId;
                    } catch (JsonParseException | JsonMappingException e) {
                        // If the parse is not successful return bad request
                        // error code.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }

                });
        /* Receive requests to add users to an existing group
         * start adding process
         */
        
        Spark.put(
                OpenAPEEndPoints.GROUP_MEMBER,
                (req, res) -> {
                	try{
                	final GroupMembershipRequest gmsr = SuperRestInterface.extractFromRequest(
                                                		GroupMembershipRequest.class, req);

                    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
                                        Group group = (Group) databaseConnection.getData(MongoCollectionTypes.GROUPS	 ,req.params(":groupId") ); 
                    
                                        String requestedUser = gmsr.getUserId();
                                        if (!requestedUser.equals(req.params(":userId")) && ProfileHandler.userExists(requestedUser) ){
                                        	res.status( 400);
                                        	return "Bad request";
                                        }
                                        
                                        if (!group.isMember(requestedUser) ) {
                                        	if (authService.getAuthenticatedUser(req,res).getId().equals(requestedUser) && ((gmsr.getStatus() == GroupMembershipStatus.APPLYED) || group.isOpenAccess )) {
                                        		GroupManagementHandler.addMember(requestedUser, gmsr.getStatus(), group);
                                        }
                	} else {
                                        GroupAuthService.allowGroupAdmin(req, res, group);
                                        GroupManagementHandler.addMember(requestedUser, gmsr.getStatus(), group);
                	}
                    return "Success";
                            } catch (IllegalArgumentException e) {
                            	res.status(404);
                            	return OpenAPEEndPoints.GROUP_DOES_NOT_EXIST;	
                            } catch (IOException e) {
                            	res.status(500);
                            	return "internal server error";
                            }
                            
                });
    }
}
