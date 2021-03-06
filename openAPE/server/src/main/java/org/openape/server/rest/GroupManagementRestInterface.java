package org.openape.server.rest;

import java.io.IOException;
import java.util.ArrayList;
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
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.mongoDB.MongoCollectionTypes;
import org.openape.server.requestHandler.GroupManagementHandler;

import spark.Spark;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupManagementRestInterface extends SuperRestInterface {

    public static void setupGroupManagementRestInterface(
            final GroupManagementHandler groupManagementHandler, final AuthService authService)
            throws IOException {

        // Enable authentication only for users
        Spark.before(OpenAPEEndPoints.GROUPS, authService.authorize("user"));
        Spark.before(OpenAPEEndPoints.GROUP_ID, authService.authorize("user"));
        Spark.before(OpenAPEEndPoints.GROUP_MEMBER, authService.authorize("user"));

        /*
         * receive requests for new resource groupsstart the creation process of
         * the group
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
                                authUser.getId(), groupRequest.isOpenAccess());
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

        Spark.put(
                OpenAPEEndPoints.GROUP_ID,
                (req, res) -> {
                    try {
                        final String groupId = req.params(":groupId");

                        final Group group = (Group) SuperRestInterface.extractObjectFromRequest(
                                req, Group.class);

                        final GroupManagementHandler groupManagement = new GroupManagementHandler();
                        group.setMembers(groupManagement.getGroupById(groupId).getMembers());

                        new GroupManagementHandler();
                        final boolean status = GroupManagementHandler.updateGroupById(groupId,
                                group);

                        if (status == true) {
                            res.status(SuperRestInterface.HTTP_STATUS_OK);
                            return "Group added";
                        } else {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return "Failed to update group informations";
                        }

                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                });

        /**
         *
         * Returns a group by a given group id
         *
         */
        Spark.get(OpenAPEEndPoints.GROUP_ID, (req, res) -> {
            try {
                final String groupId = req.params(":groupId");

                final GroupManagementHandler groupManagement = new GroupManagementHandler();
                final Group group = groupManagement.getGroupById(groupId);
                final ObjectMapper mapper = new ObjectMapper();

                res.status(SuperRestInterface.HTTP_STATUS_OK);
                return mapper.writeValueAsString(group);
            } catch (final IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

        /**
         *
         * Delets a group by a given group id
         *
         */
        Spark.delete(OpenAPEEndPoints.GROUP_ID, (req, res) -> {
            try {
                final String groupId = req.params(":groupId");
                final boolean isDeleted = new GroupManagementHandler().deleteGroupById(groupId);

                if (isDeleted == true) {

                    return "deleted";
                } else {
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                    return "could not delete group";
                }

            } catch (final IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

        Spark.get(OpenAPEEndPoints.GROUPS, (req, res) -> {
            try{
            List<Group> allGroups = new GroupManagementHandler().getAllGroups();
            final List<GroupListElement> result = new ArrayList<GroupListElement>();
            for (Group group : allGroups) {
                GroupListElement groupListElement = new GroupListElement();
                groupListElement.setGroupname(group.getName());
                groupListElement.setId(group.getId());
                groupListElement.setOwner(group.getImplementationParameters().getOwner());
                result.add(groupListElement);
            }
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonData = mapper.writeValueAsString(result);
            return jsonData;
            }catch (IOException e) {
                res.status(HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }

        });

        // Group member management
        Spark.put(
                OpenAPEEndPoints.GROUP_MEMBER,
                (req, res) -> {
                    try {
                        final DatabaseConnection databaseConnection = DatabaseConnection
                                .getInstance();
                        final String groupId = req.params(":groupId");
                        final String userId = req.params(":userId");
                        final Group group = (Group) databaseConnection.getDatabaseObjectById(
                                MongoCollectionTypes.GROUPS, req.params(":groupId"));

                        final ObjectMapper mapper = new ObjectMapper();
                        final GroupMembershipRequest gmr = mapper.readValue(req.body().toString(),
                                GroupMembershipRequest.class);

                        boolean isUserInGroup = true;

                        final List<GroupMember> listGroupMember = group.getMembers();

                        // Check if member is already in group
                        for (final GroupMember groupMember : listGroupMember) {
                            if (groupMember.getUserId().equals(userId)) {
                                res.status(409);
                                return "User already in group";
                            } else {
                                isUserInGroup = false;
                            }
                        }

                        // Assign new members to a List
                        final GroupMember groupMember = new GroupMember();
                        GroupMembershipStatus state = null;
                        if (gmr.getStatus().equals("ADMIN")) {
                            state = GroupMembershipStatus.ADMIN;
                        } else if (gmr.getStatus().equals("MEMBER")) {
                            state = GroupMembershipStatus.MEMBER;
                        } else if (gmr.getStatus().equals("IN_PROGRESS")) {
                            state = GroupMembershipStatus.IN_PROGRESS;
                        } else if (gmr.getStatus().equals("APPLYED")) {
                            state = GroupMembershipStatus.APPLYED;
                        }

                        groupMember.setUserId(userId);
                        groupMember.setState(state);
                        listGroupMember.add(groupMember);

                        group.setMembers(listGroupMember);
                        GroupManagementHandler.addMember(userId, state, group);

                        return "success";
                    } catch (final IllegalArgumentException e) {
                        res.status(404);
                        return OpenAPEEndPoints.GROUP_DOES_NOT_EXIST;
                    }

                });

        Spark.delete(
                OpenAPEEndPoints.GROUP_MEMBER,
                (req, res) -> {
                    try {
                        final DatabaseConnection databaseConnection = DatabaseConnection
                                .getInstance();
                        final String groupId = req.params(":groupId");
                        final String userId = req.params(":userId");
                        final Group group = (Group) databaseConnection.getDatabaseObjectById(
                                MongoCollectionTypes.GROUPS, req.params(":groupId"));
                        final boolean isGroupMemberInGroup = true;

                        final List<GroupMember> listGroupMember = group.getMembers();
                        final List<GroupMember> newListGroupMember = new ArrayList<GroupMember>();

                        // removes member from list
                        for (final GroupMember groupMemberEntry : listGroupMember) {
                            if (!groupMemberEntry.getUserId().equals(userId)) {
                                final GroupMember groupMember = new GroupMember();
                                groupMember.setState(groupMemberEntry.getState());
                                groupMember.setUserId(groupMemberEntry.getUserId());
                                newListGroupMember.add(groupMember);
                            }
                        }

                        group.setMembers(newListGroupMember);
                        GroupManagementHandler.updateGroupById(groupId, group);

                        return "success";
                    } catch (final IllegalArgumentException e) {
                        res.status(404);
                        return OpenAPEEndPoints.GROUP_DOES_NOT_EXIST;
                    }
                });

        /*
         * Receive requests to add users to an existing group start adding
         * process
         */

        /*
         * Spark.put( OpenAPEEndPoints.GROUP_MEMBER, (req, res) -> { try { final
         * GroupMembershipRequest gmsr = SuperRestInterface.extractFromRequest(
         * GroupMembershipRequest.class, req);
         * 
         * final DatabaseConnection databaseConnection = DatabaseConnection
         * .getInstance(); final Group group = (Group)
         * databaseConnection.getDatabaseObjectById(
         * MongoCollectionTypes.GROUPS, req.params(":groupId"));
         * 
         * final String requestedUser = gmsr.getUserId(); if
         * (!requestedUser.equals(req.params(":userId")) &&
         * ProfileHandler.userExists(requestedUser)) { res.status(400); return
         * "Bad request"; }
         * 
         * if (!group.isUserAssigend(requestedUser)) { if
         * (authService.getAuthenticatedUser(req, res).getId()
         * .equals(requestedUser) && ((gmsr.getStatus() ==
         * GroupMembershipStatus.APPLYED) || group .isOpenAccess())) {
         * GroupManagementHandler.addMember(requestedUser, gmsr.getStatus(),
         * group); } } else { final GroupAuthService groupAuthService = new
         * GroupAuthService(); groupAuthService.allowOpenAPEAndGroupAdmin(req,
         * res, group); GroupManagementHandler.addMember(requestedUser,
         * gmsr.getStatus(), group); } return "Success"; } catch (final
         * IllegalArgumentException e) { res.status(404); return
         * OpenAPEEndPoints.GROUP_DOES_NOT_EXIST; } catch (final IOException e)
         * { res.status(500); return "internal server error"; }
         * 
         * });
         */
    }

}
