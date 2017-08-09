package org.openape.server.api.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openape.api.groups.GroupMembershipStatus;

/**
 * This class provides helper functions to edit the group memberships via a HTML
 * text area.
 *
 * This class is thread safe.
 *
 * @author Tobias Ableitner
 *
 */
public class GroupManagementHelper {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// abstract methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// override methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// public methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a list with group members from HTML text area, which is used to
	 * define group member ships.
	 *
	 * @param groupMembersAsString
	 *            value of the group members text area. It must not be null!
	 * @param locale
	 *            is needed for language specific error messages
	 * @param userNameToUserIdMap
	 *            map which contains all user names as key and all user IDs as
	 *            corresponding values. It must not be null!
	 * @return list with group members. If the text area respectively the
	 *         parameter groupMembersAsString is empty, an empty list will be
	 *         returned.
	 */
	public static List<GroupMember> createGroupMembersFromTextArea(final String groupMembersAsString,
			final Locale locale, final Map<String, String> userNameToUserIdMap) {
		final List<GroupMember> groupMembers = new ArrayList<GroupMember>();
		final String regexLine = "[^ ]+([ ]admin)?";

		final String[] lines = groupMembersAsString.split(new String(new char[] { 13, 10 }));

		if ((groupMembersAsString.isEmpty() == false)
				&& (groupMembersAsString.equals("username [admin][linebreak]") == false)) {
			for (final String line : lines) {

				// check line format
				if (line.matches(regexLine) == false) {
					final String message = "Invalid format in line = " + line + "!";

					// TODO throw exception

					// String userMessage =
					// TemplateFiller.fillTemplate(LanguageHandler.getWord(locale,
					// "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_INVALID_LINE"),
					// line);
					// throw new BadRequestException(message, null,
					// userMessage);
				}

				// check whether group member should be group admin
				GroupMembershipStatus state = GroupMembershipStatus.MEMBER;
				String userName = line;
				if (line.contains(" admin")) {
					userName = line.substring(0, line.indexOf(" "));
					state = GroupMembershipStatus.ADMIN;
				}

				// check whether group member exists or not
				if (userNameToUserIdMap.containsKey(userName)) {
					groupMembers.add(new GroupMember(userNameToUserIdMap.get(userName), state));
				} else {
					final String message = "Unknown userName = " + userName + "!";

					// TODO throw exception

					// String userMessage =
					// TemplateFiller.fillTemplate(LanguageHandler.getWord(locale,
					// "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_UNKNOWN_USER_NAME"),
					// userName);
					// throw new BadRequestException(message, null,
					// userMessage);
				}

			}// end for loop
		}
		return groupMembers;
	}

	/**
	 * Creates the content as string for the HTML text area, which used to edit
	 * the group memberships.
	 *
	 * @param members
	 *            the group members. It must not be null!
	 * @param userIDToUserNameMap
	 *            map which contains all user IDs as key and all user names as
	 *            corresponding values. It must not be null!
	 * @return String representing the group memberships for the HTML text area
	 */
	public static String groupMembersToStringValue(final List<GroupMember> members,
			final Map<String, String> userIDToUserNameMap) {
		String groupMembersAsString = "";
		for (final GroupMember groupMember : members) {
			final String userName = userIDToUserNameMap.get(groupMember.getUserId());
			if (userName != null) {
				if (groupMember.getState() == GroupMembershipStatus.MEMBER) {
					groupMembersAsString += userName;
					if (groupMember.getState() == GroupMembershipStatus.ADMIN) {
						groupMembersAsString += " admin";
					}
				}
				groupMembersAsString += "\n";
			}
		}
		return groupMembersAsString;
	}

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// protected methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// private methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
