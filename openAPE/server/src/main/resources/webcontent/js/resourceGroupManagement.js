/**
 * Provides all functions required to add, edit and delete group access rights.
 * 
 * @author Lukas Smirek
 */
$(document).ready(
		function() {

			/**
			 * when the edit resource button is pressed the system preloads the
			 * group access table with all groups the resource belongs to
			 */
			$('.editResourceBtn').click(function() {
				var resourceId = $('#editInputResourceId').val();
				var accessRights = getAccesRightsOfResourceFromDB(resourceId);				
				var accessRightList = accessRights.groupAccessRights;
				for(var i = 0; i < accessRightList.length; i++) {
					var accessRight = accessRightList[i];
					var groupName = getGroupByIdFromDB(accessRight.groupId);
					 $('#editResource_resourceGroupDataTable').append(
							 addTableRowWithAccessRight(accessRight,
							 groupName));
				}

			})

			/*
			 * Called when a resource is added to a group. Important, only
			 * resource and group are only connected in the GUI, separate
			 * confirmation and upload to server required
			 */
			$('#addResource_btnAddResourceToGroup').click(
					function() {
						var groupId = $('#addResource_inputGroupId').val();
						var group = getGroupByIdFromDB(groupId);
						var resourceId = null;
						var accessRight = new openape_api.AccessRight(groupId,
								resourceId, false, false, false, false);
						$('#addResource_resourceGroupDataTable').append(
								addTableRowWithAccessRight(accessRight,
										group.name));
					})

			/*
			 * Called when a resource is added to a group. Important, only
			 * resource and group are only connected in the GUI, separate
			 * confirmation and upload to server required
			 */
			$('#editResource_btnAddResourceToGroup').click(
					function() {
						var groupId = $('#editResource_inputGroupId').val();
						var group = getGroupByIdFromDB(groupId);
						var resourceId = null;
						var accessRight = new openape_api.AccessRight(groupId,
								resourceId, false, false, false, false);
						$('#editResource_resourceGroupDataTable').append(
								addTableRowWithAccessRight(accessRight,
										group.name));
					})

			// /*
			// * Called when the add resource dialog is opened. Preload all
			// groups
			// */
			// $('#btnAddResource').click(
			// function() {
			// var groups = getGroupsFromDB();
			// for (var i = 0; i < groups.length; i++) {
			// var groupId = groups[i].id;
			// var resourceId = null;
			// var accessRight = new openape_api.AccessRight(
			// groupId, resourceId, false, false, false,
			// false);
			// var groupName = groups[i].groupname;
			// $('#resourceGroupDataTable').append(
			// addTableRowWithAccessRight(accessRight,
			// groupName));
			// // console.log(addTableRowWithAccessRight(accessRight));
			// }
			// })

			/*
			 * Called when a resource is added to a group. Important, only
			 * resource and group are only connected in the GUI, separate
			 * confirmation and upload to server required
			 */
			$('#addResource_btnAddResourceToGroup').click(function() {
				var groupId = $('#addResource_inputGroupId').val();
			})

			/*
			 * Called when a resource is added to a group. Important, only
			 * resource and group are only connected in the GUI, separate
			 * confirmation and upload to server required
			 */
			$('#editResource_btnAddResourceToGroup').click(function() {
				var groupId = $('#editResource_inputGroupId').val();
			})

			/**
			 * creates an HTML string that represents a new row in the table
			 * with a groups access right on a resource
			 * 
			 * @param accessRight
			 *            Object of type openape_api.AccessRight
			 * @return String with HTML representation of a table row
			 */
			function addTableRowWithAccessRight(accessRight, groupName) {
				return '<tr>'
						+ '<td id=\'accessRight_'
						+ accessRight.groupId
						+ '_groupId\' >'
						+ accessRight.groupId
						+ '</td>'
						+ '<td>'
						+ groupName
						+ '</td>'
						+ createTd('readRight', accessRight.readRight,
								accessRight.groupId)
						+ createTd('updateRight', accessRight.updateRight,
								accessRight.groupId)
						+ createTd('deleteRight', accessRight.deleteRight,
								accessRight.groupId)
						+ createTd('changeRight', accessRight.changeRight,
								accessRight.groupId) + '</tr>';
			}
			/*
			 * Creates a table data element, element contains a checkbox with
			 * the id accessRight<groupId>_<accessRightType> @param idSuffix
			 * string that is part of the checkboxe's id and indicates if it is
			 * readRight, updateRight, deleteRight or changeRight @param value
			 * indicates if an access right is true or false @groupId the id of
			 * the group to which the access right is bound
			 */
			function createTd(idSuffix, value, groupId) {
				return '<td><input id=\'input_accessRight_' + groupId + '_'
						+ idSuffix + '\' type=\'checkbox\'' + isChecked(value)
						+ '></input></td>\n';
			}
			/*
			 * Creates the string that indicates if a checkbox contains a
			 * checked attribute or not @return String
			 */
			function isChecked(value) {
				if (value) {
					return 'checked'
				}
				return '';
			}

			function getGroupByIdFromDB(id) {
				var objGroup = {};
				$.ajax({
					type : 'GET',
					contentType : 'application/json',
					url : url + '/openape/groups/' + id,
					async : false,
					headers : {
						"Authorization" : localStorage.getItem("token"),
					},
					success : function(data, textStatus, jqXHR) {
						objGroup = JSON.parse(jqXHR.responseText);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
					}
				});
				return objGroup;
			}

			/**
			 * get all Groups from server
			 * 
			 * @param groupId
			 * @returns
			 */
			function getGroupsFromDB() {
				var objGroups = {};
				$.ajax({
					type : 'GET',
					contentType : 'application/json',
					url : url + '/openape/groups',
					async : false,
					headers : {
						"Authorization" : localStorage.getItem("token"),
					},
					success : function(data, textStatus, jqXHR) {
						objGroups = JSON.parse(jqXHR.responseText);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
					}
				});

				return objGroups;
			}

			/**
			 * get Head of resource.
			 */
			function getAccesRightsOfResourceFromDB(resourceId) {
				var objAccessRights = {};
				$.ajax({
					type : 'HEAD',
					contentType : 'application/json',
					url : url + '/api/resources/' + resourceId,
					async : false,
					headers : {
						"Authorization" : localStorage.getItem("token"),
					},
					success : function(data, textStatus, jqXHR) {
						objAccessRights = JSON.parse(jqXHR.getResponseHeader("groupAccessRights"));
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
					}
				});
				return objAccessRights;
			}
		})
