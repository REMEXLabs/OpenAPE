(function(window){
	 function defineOpenape(){
	    var objOpenape = {};
	   
	    //get the protocol and address of the location. If it´s running local, than the address should be http://localhost:4567
	    
	    var token = "";
	    
	    /*
		    * initializeLibrary
		    */
		  
		    /** initializeLibrary
			* 
			* This function is used get the authorization token for the given grantTypem, username and password 
			*
			* 	 
		    * @param  userName
		    * 	 The username of the user from the mongodb
			* 			 
		    * @param  password
		    * 	 The password of the user from the mongodb
		    * 
			* @return      
			* 	 A javascript object with all token information
			*/
		    objOpenape.initializeLibrary = function (username, password, serverurl) {
		    	if(serverurl === undefined){
		    		localStorage.setItem("host", "http://openape.gpii.eu");
		    	} else if(serverurl === "/"){
		    		localStorage.setItem("host", location.protocol);
		    	} else {
		    		localStorage.setItem("host", serverurl);
		    	}
		    		
		    		
		    	var objToken = {};
		    	var objAjaxParameters = {};
		    	
		    	var arrStatusText = [];
		    
		    	var isPasswordCorret = true;
		    	var isUsernameCorrect = true;
		    	
		    	if(username == ""){
		    		arrStatusText.push("Username can not be empty");
		    		isUsernameCorrect = false;
		    	} else if(username === undefined){
		    		arrStatusText.push("Please enter a username");
		    		isUsernameCorrect = false;
		    	}
		    	
		    	if(password==""){
		    		arrStatusText.push("Password can not be empty");
		    		isPasswordCorret = false;
		    	} else if(password === undefined){
		    		arrStatusText.push("Please enter a password");
		    		isUsernameCorrect = false;
		    	}
		    	
		    	if(isPasswordCorret && isUsernameCorrect){
		    		objAjaxParameters.type = "POST";
		    		objAjaxParameters.url = localStorage.getItem("host")+"/token";
		    		objAjaxParameters.contentType = 'application/x-www-form-urlencoded';
		    		objAjaxParameters.data = "grant_type=password&username="+encodeURIComponent(username)+"&password="+encodeURIComponent(password),
		    		objToken = databaseCommunication(objAjaxParameters);
		    	} else {
		    		objToken.statusText = arrStatusText;
		    		objToken.status = 400;
		    	}
		    	localStorage.setItem("token", JSON.parse(objToken.responseText).access_token);
		    	return objToken;
		    }
		    
	    //
	    // FUNCTIONS FOR USER
	    //
	    
	    /** createUser
	     * 
	     * This function is used to create the user in the mongodb database with the given username, email and password
	     *
	     * @param  userName
	     * 	 The username of the user from the mongodb
	     * 
	     * @param  email
	     * 	 The email address of the user from the mongodb
	     * 
	     * @param  password
	     * 	 The password of the user from the mongodb
	     * 
	     * @return      The function will send the user credentials to the function
	     * 				sendUserData and return a status as a boolean 
	     */
	    objOpenape.createUser = function (username, email, password, serverurl) {
	    	if(serverurl === undefined){
	    		localStorage.setItem("host", "http://openape.gpii.eu");
	    	} else if(serverurl === "/"){
	    		localStorage.setItem("host", location.protocol);
	    	} else {
	    		localStorage.setItem("host", serverurl);
	    	}
	    	
	   		var objUser = new Object();
   			var objSendUserdata = {};
	    	var objAjaxParameters = {};
	    	
	   		var arrRoles = [];
	   		
	   		var objErrResponse = new Object();
	   		var arrStatusTexts = [];
	   		
	   		var isUsernameCorrect = false;
	   		var isEmailCorrect = false;
	   		var isPasswordCorrect = false;
	    	
	   		arrRoles.push("user");
	   		
	   		//check if username is correct
	   		if(username==""){
	   			isUsernameCorrect = false;
	   			arrStatusTexts.push("The username can not be empty");
	   		} else if(username === undefined){
	   			isUsernameCorrect = false;
	   			arrStatusTexts.push("Please enter a username");
	   		} else {
	   			objUser.username = username;
	   			isUsernameCorrect = true;
	   		}
	    		
	   		//check if email is correct
	   		if(email==""){  			
	   			isEmailCorrect=false;
	   			arrStatusTexts.push("The email can not be empty");
			} else if(email === undefined){
				isEmailCorrect = false;
	   			arrStatusTexts.push("Please enter a email");
			} else {
	   			if((validateEmail(email))==true){
	   	   			objUser.email = email; 			
	   	   			isEmailCorrect=true;
	   			} else {
	   				arrStatusTexts.push("Wrong email form");
	   			}
	  		}
	    		
	   		//check if password is correct
	   		if(password==""){
	   			isPasswordCorrect=false;
	   			arrStatusTexts.push("The password can not be empty");
	   		} else if(password === undefined){
	   			arrStatusTexts.push("Please enter a password");
	   		} else {
	   			objUser.password = password;
	   			isPasswordCorrect=true;
	   		}    		
	    		
	   		if(isPasswordCorrect && isEmailCorrect && isUsernameCorrect){
	   			objUser.roles = arrRoles;
		    	objAjaxParameters.data = JSON.stringify(objUser);
		    	objAjaxParameters.type = "POST";
		    	objAjaxParameters.url = localStorage.getItem("host")+"/users";
		    	objAjaxParameters.contentType = 'application/json';
		    	objSendUserdata = databaseCommunication(objAjaxParameters);
		    	
		    	if(objSendUserdata.responseText.includes("username_1 dup key")){
 	        	   objSendUserdata.statusText = "User with this username already exists";
 		       } else  if(objSendUserdata.responseText.includes("email_1 dup key")){
 		    	   objSendUserdata.statusText = "User with this email already exists";
 		       } 
	   			
	   			return objSendUserdata;
	   		} else {
	   			objErrResponse.status = 400;
	   			objErrResponse.statusText = arrStatusTexts;
	   			return objErrResponse;
	   		}
	    }

	    /** getUser
	    * 
	    * This function is used to get user informations by the given token
	    *
	    * @param  token
	    * 	 The authentification token to authorized the user
	    * 
	    * @return      
	    * 	 A javascript object with all found user information like username, email and roles
	    */
	    objOpenape.getUser = function () {
	    	var objUserProfile = {};
	    	var objAjaxParameters = {};
	    	
	    	var isTokenCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		objUserProfile.statusText = "Please initialize the library";
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(isTokenCorrect) {
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/profile";
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUserProfile = databaseCommunication(objAjaxParameters);
	    		
	    	} else {
	    		objUserProfile.status = 400;
	    	}
	    	
	    	return objUserProfile;
	    }
	    
	    
	    /** getUserById
		    * 
		    * This function is used to get user informations by the given token
		    *
		    * @param  token
		    * 	 The authentification token to authorized the user
		    * 
		    * @return      
		    * 	 A javascript object with all found user information like username, email and roles
		    */
		    objOpenape.getUserById = function (userId) {
		    	var objUserProfile = {};
		    	var objAjaxParameters = {};
		    	
		    	var isTokenCorrect = true;
		    	
		    	if(localStorage.getItem("token") === undefined){
		    		objUserProfile.statusText = "Please initialize the library";
		    		isTokenCorrect = false;
		    	} 
		    	
		    	if(isTokenCorrect) {
		    		objAjaxParameters.type = "GET";
		    		objAjaxParameters.url = localStorage.getItem("host")+"/users/openape/"+userId;
		    		objAjaxParameters.token = localStorage.getItem("token");
		    		objUserProfile = databaseCommunication(objAjaxParameters);
		    		
		    	} else {
		    		objUserProfile.status = 400;
		    	}
		    	
		    	return objUserProfile;
		    }
	   
	    
	    /*
	     * USER-CONTEXTS FUNCTION
	     */
	   
	    /** getUserContext
		* 
		* This function is used get the authorization token for the given grantTypem, username and password 
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored UserContextId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all user contexts information
		*/
	    objOpenape.getUserContext = function (userContextId, outputType) {
	    	var objGetUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	localStorage.setItem("host", "http://"+window.location.host);
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isuserContextIdCorrect){
	    		if(outputType == "JSON"){
	    			objAjaxParameters.contentType = "application/json";
	    		} else {
	    			objAjaxParameters.contentType = "application/xml";
	    		}
	    		
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/user-contexts/"+userContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objGetUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objGetUserContext_Result.status = 400;
	    		objGetUserContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetUserContext_Result;
	    }
	    
	    /** createUserContext
		* 
		* This function is used to create the user contexts  
		*
		* @param  UserContext
	    * 	 The given user contexts
		*
		* @param  token
		* 	 The authentification token to authorized the user
	    * 
		* @return      
		* 	 A javascript object with all status information of the create process
		*/	    
	    objOpenape.createUserContext = function (UserContext, contentType) {
	    	var objcreateUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(UserContext==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(UserContext === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isUserContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isUserContextCorrect){
	    		objAjaxParameters.data = UserContext;
	    		objAjaxParameters.type = "POST";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/user-contexts";
	    		
	    		switch (contentType){
	    			case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
	    			case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    	
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objcreateUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objcreateUserContext_Result.statusText = arrStatusText;
	    		objcreateUserContext_Result.status = 400;
	    	}
	    	return objcreateUserContext_Result;
	    }
	    
	    /** deleteUserContext
		* 
		* This function is used to delete user contexts  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored UserContextId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all status information of the delete process
		*/	    	    
	    objOpenape.deleteUserContext = function (userContextId) {
	    	var objDeleteUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isuserContextIdCorrect ){
	    		objAjaxParameters.type = "DELETE";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/user-contexts/"+userContextId,
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objDeleteUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objDeleteUserContext_Result.status = 400;
	    	}
	    	return objDeleteUserContext_Result;
	    }
	    
	    /** updateUserContext
		* 
		* This function is used to update user contexts by the given usercontext id  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored UserContextId from mongodb
	    * 
		* @param  UserContext
	    * 	 The given user contexts
	    * 
		* @return      
		* 	 A javascript object with all status information of the update process
		*/	
	    objOpenape.updateUserContext = function (userContextId, UserContext, contentType) {
	    	var objUpdateUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(UserContext==""){
	    		arrStatusText.push("The usercontext can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(UserContext === undefined){
	    		arrStatusText.push("Please enter a usercontext");
	    		isUserContextCorrect = false;
	    	}
	    	
	    	if(userContextId==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isuserContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isuserContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isUserContextCorrect && isuserContextIdCorrect ){
	    		objAjaxParameters.data = UserContext;
	    		objAjaxParameters.type = "PUT";
	    		
	    		switch (contentType){
    				case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
    				case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/user-contexts/"+userContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUpdateUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdateUserContext_Result.status = 400;
	    		objUpdateUserContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateUserContext_Result;
	    }     
	    
	    
	    
	    /*
	     * 
	     * Task-Contexts Functions
	     */
	    
	    /** createTaskContext
	     * 
	     * This function is used to create the task-context in the mongodb database with the given task-context
	     *
	     * @param  TaskContext
	     * 	 The given task-context in JSON
	     * 
	     * @return      An JavaScript-Object with the create result
	     */
	    objOpenape.createTaskContext = function (taskContext, contentType) {
	    	var objcreateTaskContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isTaskContextCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTaskContextCorrect = false;
	    	} 
	    	
	    	if(taskContext==""){
	    		arrStatusText.push("The taskcontext can not be empty");
	    		isTaskContextCorrect = false;
	    	} else if(taskContext === undefined){
	    		arrStatusText.push("Please enter a taskcontext");
	    		isTaskContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isTaskContextCorrect){	
	    		switch (contentType){
    				case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
    				case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.data = taskContext;
	    		objAjaxParameters.type = "POST";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/task-contexts";
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objcreateTaskContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objcreateTaskContext_Result.statusText = arrStatusText;
	    		objcreateTaskContext_Result.status = 400;
	    	}
	    	return objcreateTaskContext_Result;
	    }
	    
	    /** getTaskContext
	     * 
	     * This function is used to get the task-context from the Mongodb database with the given taskContextId
	     *
	     * @param  taskContextId
	     * 	 The taskContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the get result and the corresponding task-context to the given taskContextId
	     */
	    objOpenape.getTaskContext = function (taskContextId, outputType) {
	    	var objGetTaskContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isTaskContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(taskContextId==""){
	    		arrStatusText.push("The taskContextId can not be empty");
	    		isTaskContextIdCorrect = false;
	    	} else if(taskContextId === undefined){
	    		arrStatusText.push("Please enter a taskContextId");
	    		isTaskContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isTaskContextIdCorrect){
	    		if(outputType == "JSON"){
	    			objAjaxParameters.contentType = "application/json";
	    		} else {
	    			objAjaxParameters.contentType = "application/xml";
	    		}
	    		
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/task-contexts/"+taskContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objGetTaskContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objGetTaskContext_Result.status = 400;
	    		objGetTaskContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetTaskContext_Result;
	    }

	    /** deleteTaskContext
	     * 
	     * This function is used to delete the task-context from the Mongodb database with the given taskContextId
	     *
	     * @param  taskContextId
	     * 	 The taskContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the delete result
	     */
	    objOpenape.deleteTaskContext = function (taskContextId) {
	    	var objDeleteTaskContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isTokenCorrect = true;
	    	var isTaskContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(taskContextId==""){
	    		arrStatusText.push("The taskcontextId can not be empty");
	    		isTaskContextIdCorrect = false;
	    	} else if(taskContextId === undefined){
	    		arrStatusText.push("Please enter a taskcontextId");
	    		isTaskContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isTaskContextIdCorrect ){
	    		objAjaxParameters.type = "DELETE";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/task-contexts/"+taskContextId,
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objDeleteTaskContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objDeleteTaskContext_Result.statusText = arrStatusText;
	    		objDeleteTaskContext_Result.status = 400;
	    	}
	    	return objDeleteTaskContext_Result;
	    }
	    
	    /** updateTaskContext
	     * 
	     * This function is used to update the task-context in MongoDB with the given taskContextId and task-context
	     *
	     * @param  taskContextId
	     * 	 The taskContextId
	     * 
	     * @param  TaskContext
	     * 	 The given task-context in JSON
	     * 
	     * @return      
	     * 	An JavaScript-Object with the update result
	     */
	    objOpenape.updateTaskContext = function (taskContextId, taskContext, contentType) {
	    	var objUpdateUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isTaskContextCorrect = true;
	    	var isTaskContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(taskContext==""){
	    		arrStatusText.push("The taskcontext can not be empty");
	    		isTaskContextCorrect = false;
	    	} else if(taskContext === undefined){
	    		arrStatusText.push("Please enter a taskcontext");
	    		isTaskContextCorrect = false;
	    	}
	    	
	    	if(taskContextId==""){
	    		arrStatusText.push("The taskContextId can not be empty");
	    		isTaskContextIdCorrect = false;
	    	} else if(taskContextId === undefined){
	    		arrStatusText.push("Please enter a taskContextId");
	    		isTaskContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isTaskContextCorrect && isTaskContextIdCorrect ){
	    		objAjaxParameters.data = taskContext;
	    		objAjaxParameters.type = "PUT";
	    		
	    		switch (contentType){
					case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
					case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/task-contexts/"+taskContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUpdateUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdateUserContext_Result.status = 400;
	    		objUpdateUserContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateUserContext_Result;
	    }     
	    

	    /*
	     * Equipment-Contexts functions
	     */

	    /** getEquipmentContext
	     * 
	     * This function is used to get the equipment-context from the Mongodb database with the given equipmentContextId
	     *
	     * @param  equipmentContextId
	     * 	 The equipmentContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the equipment-context result
	     */
	    objOpenape.getEquipmentContext = function (equipmentContextId, outputType) {
	    	var objGetEquipmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEquipmentContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(equipmentContextId==""){
	    		arrStatusText.push("The equipmentContextId can not be empty");
	    		isEquipmentContextIdCorrect = false;
	    	} else if(equipmentContextId === undefined){
	    		arrStatusText.push("Please enter a equipmentContextId");
	    		isEquipmentContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEquipmentContextIdCorrect){
	    		if(outputType == "JSON"){
	    			objAjaxParameters.contentType = "application/json";
	    		} else {
	    			objAjaxParameters.contentType = "application/xml";
	    		}
	    		
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/equipment-contexts/"+equipmentContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objGetEquipmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objGetEquipmentContext_Result.status = 400;
	    		objGetEquipmentContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetEquipmentContext_Result;
	    }
	    

	    /** deleteEquipmentContext
	     * 
	     * This function is used to delete the equipment-context from the Mongodb database with the given equipmentContextId
	     *
	     * @param  equipmentContextId
	     * 	 The equipmentContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the delete result
	     */
	    objOpenape.deleteEquipmentContext = function (equipmentContextId) {
	    	var objDeleteEquipmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isTokenCorrect = true;
	    	var isEquipmenContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(equipmentContextId==""){
	    		arrStatusText.push("The equipmentContextId can not be empty");
	    		isEquipmenContextIdCorrect = false;
	    	} else if(equipmentContextId === undefined){
	    		arrStatusText.push("Please enter a equipmentContextId");
	    		isEquipmenContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEquipmenContextIdCorrect ){
	    		objAjaxParameters.type = "DELETE";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/equipment-contexts/"+equipmentContextId,
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objDeleteEquipmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objDeleteEquipmentContext_Result.statusText = arrStatusText;
	    		objDeleteEquipmentContext_Result.status = 400;
	    	}
	    	return objDeleteEquipmentContext_Result;
	    }

	    /** createEquipmentContext
	     * 
	     * This function is used to create the equipment-context in the mongodb database with the given equipment-context
	     *
	     * @param  EquipmentContext
	     * 	 The given equipment-context in JSON
	     * 
	     * @return      
	     * 	 An JavaScript-Object with the create result
	     */	    
	    objOpenape.createEquipmentContext = function (equipmentContext, contentType) {
	    	var objcreateEquipmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEquipmentContextCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(equipmentContext==""){
	    		arrStatusText.push("The equipmentContext can not be empty");
	    		isEquipmentContextCorrect = false;
	    	} else if(equipmentContext === undefined){
	    		arrStatusText.push("Please enter a equipmentContext");
	    		isEquipmentContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEquipmentContextCorrect){	
	    		objAjaxParameters.data = equipmentContext;
	    		objAjaxParameters.type = "POST";
	    		
	    		switch (contentType){
					case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
					case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/equipment-contexts";
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objcreateEquipmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objcreateEquipmentContext_Result.statusText = arrStatusText;
	    		objcreateEquipmentContext_Result.status = 400;
	    	}
	    	return objcreateEquipmentContext_Result;
	    }
	    
	    /** updateEquipmentContext
	     * 
	     * This function is used to update the equipment-context in MongoDB with the given equipmentContextId and equipmentContext
	     *
	     * @param  equipmentContextId
	     * 	 The equipmentContextId
	     * 
	     * @param  equipmentContext
	     * 	 The given equipmentContext in JSON
	     * 
	     * @return      
	     * 	An JavaScript-Object with the update result
	     */
	    objOpenape.updateEquipmentContext = function (equipmentContextId, equipmentContext, contentType) {
	    	var objUpdateEquipmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEquipmentContextCorrect = true;
	    	var isEquipmentContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(equipmentContext==""){
	    		arrStatusText.push("The EquipmentContext can not be empty");
	    		isEquipmentContextCorrect = false;
	    	} else if(equipmentContext === undefined){
	    		arrStatusText.push("Please enter a EquipmentContext");
	    		isEquipmentContextCorrect = false;
	    	}
	    	
	    	if(equipmentContextId==""){
	    		arrStatusText.push("The equipmentContextId can not be empty");
	    		isEquipmentContextIdCorrect = false;
	    	} else if(equipmentContextId === undefined){
	    		arrStatusText.push("Please enter a equipmentContextId");
	    		isTaskContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEquipmentContextCorrect && isEquipmentContextIdCorrect ){
	    		objAjaxParameters.data = equipmentContext;
	    		objAjaxParameters.type = "PUT";
	    		
	    		switch (contentType){
					case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
					case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/equipment-contexts/"+equipmentContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUpdateEquipmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdateEquipmentContext_Result.status = 400;
	    		objUpdateEquipmentContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateEquipmentContext_Result;
	    }     
	    
	    
	    /*
	     * EnvironmentContextronment-Contexts functions
	     */

	    /** getEnvironmentContext
	     * 
	     * This function is used to get the environment-context from the MongoDB database with the given environmentContextId
	     *
	     * @param  environmentContextId
	     * 	 The environmentContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the environment-context result
	     */
	    objOpenape.getEnvironmentContext = function (environmentContextId, outputType) {
	    	var objGetEnvironmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEnvironmentContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(environmentContextId==""){
	    		arrStatusText.push("The environmentContextId can not be empty");
	    		isEnvironmentContextIdCorrect  = false;
	    	} else if(environmentContextId === undefined){
	    		arrStatusText.push("Please enter a environmentContextId");
	    		isEnvironmentContextIdCorrect  = false;
	    	}
	    	
	    	if(isTokenCorrect && isEnvironmentContextIdCorrect ){
	    		if(outputType == "JSON"){
	    			objAjaxParameters.contentType = "application/json";
	    		} else {
	    			objAjaxParameters.contentType = "application/xml";
	    		}
	    		
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/environment-contexts/"+environmentContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objGetEnvironmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objGetEnvironmentContext_Result.status = 400;
	    		objGetEnvironmentContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetEnvironmentContext_Result;
	    }
	    

	    /** deleteEnvironmentContext
	     * 
	     * This function is used to delete the environment-context from the Mongodb database with the given environmentContextId
	     *
	     * @param  equipmentContextId
	     * 	 The equipmentContextId
	     * 
	     * @return      
	     * 	An JavaScript-Object with the delete result
	     */
	    objOpenape.deleteEnvironmentContext = function (environmentContextId) {
	    	var objDeleteEnvironmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isTokenCorrect = true;
	    	var isEnvironmentContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(environmentContextId==""){
	    		arrStatusText.push("The environmentContextId can not be empty");
	    		isEnvironmentContextIdCorrect = false;
	    	} else if(environmentContextId === undefined){
	    		arrStatusText.push("Please enter a environmentContextId");
	    		isEnvironmentContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEnvironmentContextIdCorrect ){
	    		objAjaxParameters.type = "DELETE";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/environment-contexts/"+environmentContextId,
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objDeleteEnvironmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objDeleteEnvironmentContext_Result.statusText = arrStatusText;
	    		objDeleteEnvironmentContext_Result.status = 400;
	    	}
	    	return objDeleteEnvironmentContext_Result;
	    }

	    /** createEnvironmentContext
	     * 
	     * This function is used to create the environment-context in the MongoDB database with the given environment-context
	     *
	     * @param  EnvironmentContext
	     * 	 The given environment-context in JSON
	     * 
	     * @return      
	     * 	 An JavaScript-Object with the create result
	     */	    
	    objOpenape.createEnvironmentContext = function (environmentContext, contentType) {
	    	var objcreateEnvironmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEnvironmentContextCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(environmentContext==""){
	    		arrStatusText.push("The environmentContext can not be empty");
	    		isEnvironmentContextCorrect = false;
	    	} else if(environmentContext === undefined){
	    		arrStatusText.push("Please enter a environmentContext");
	    		isEnvironmentContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEnvironmentContextCorrect){	
	    		objAjaxParameters.data = environmentContext;
	    		objAjaxParameters.type = "POST";
	    		
	    		switch (contentType){
    				case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
    				case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/environment-contexts";
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objcreateEnvironmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objcreateEnvironmentContext_Result.statusText = arrStatusText;
	    		objcreateEnvironmentContext_Result.status = 400;
	    	}
	    	return objcreateEnvironmentContext_Result;
	    }
	    
	    /** updateEnvironmentContext
	     * 
	     * This function is used to update the environment-context in MongoDB with the given environmentContextId and EnvironmentContext
	     *
	     * @param  environmentContextId
	     * 	 The environmentContextId
	     * 
	     * @param  environmentContext
	     * 	 The given environmentContext in JSON
	     * 
	     * @return      
	     * 	An JavaScript-Object with the update result
	     */
	    objOpenape.updateEnvironmentContext = function (environmentContextId, environmentContext, contentType) {
	    	var objUpdateEnvironmentContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isEnvironmentContextCorrect = true;
	    	var isEnvironmentContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(environmentContext==""){
	    		arrStatusText.push("The environmentContext can not be empty");
	    		isEnvironmentContextCorrect = false;
	    	} else if(environmentContext === undefined){
	    		arrStatusText.push("Please enter a environmentContext");
	    		isEnvironmentContextCorrect = false;
	    	}
	    	
	    	if(environmentContextId==""){
	    		arrStatusText.push("The environmentContextId can not be empty");
	    		isEnvironmentContextIdCorrect = false;
	    	} else if(environmentContextId === undefined){
	    		arrStatusText.push("Please enter a environmentContextId");
	    		isEnvironmentContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isEnvironmentContextCorrect && isEnvironmentContextIdCorrect ){
	    		objAjaxParameters.data = environmentContext;
	    		objAjaxParameters.type = "PUT";
	    		
	    		switch (contentType){
					case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
					case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		}
	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+"/api/environment-contexts/"+environmentContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUpdateEnvironmentContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdateEnvironmentContext_Result.status = 400;
	    		objUpdateEnvironmentContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateEnvironmentContext_Result;
	    } 
	    
		/**  Executes 
		* @returns JQXHR object */
	    function databaseCommunication (objAjaxParameters) {
	    	var objStatus = {};
	    	var request =  
	    	{
	    		async: false,	
		    	success: function(data, textStatus, jqXHR){
		    		objStatus = jqXHR;
		    	},
		    	error: function(jqXHR, textStatus, errorThrown){
		    		objStatus = jqXHR;
		    	}
	    	}
	    	
	    	if(objAjaxParameters.type == "PUT") {
	    		request.data = objAjaxParameters.data;
	    		request.type = objAjaxParameters.type;
	    		request.url = objAjaxParameters.url;
	    		request.contentType = objAjaxParameters.contentType;
	    		
	    		if (objAjaxParameters.token !== undefined) {
	    			request.headers = {
			    	   	 "Authorization": objAjaxParameters.token,
			    	}
	    		}
	    	}  else if(objAjaxParameters.type == "DELETE"){
	    		request.type = objAjaxParameters.type;
	    		request.url = objAjaxParameters.url;
	    		
	    		if (objAjaxParameters.token !== undefined) {
	    			request.headers = {
			    	   	 "Authorization": objAjaxParameters.token,
			    	}
	    		}
	    	} else if(objAjaxParameters.type == "GET"){
	    		request.type = objAjaxParameters.type;
	    		request.url = objAjaxParameters.url;
	    		request.contentType = objAjaxParameters.contentType;
	    		
	    		if (objAjaxParameters.token != null) {
	    			request.headers = {
			    	   	 "Authorization": objAjaxParameters.token,
			    	}
	    		}
	    	} else if(objAjaxParameters.type == "POST"){
	    		request.type = objAjaxParameters.type;
	    		request.url = objAjaxParameters.url;
	    		request.contentType = objAjaxParameters.contentType;
	    		
	    		if (objAjaxParameters.token !== undefined) {
	    			request.headers = {
			    	   	 "Authorization": objAjaxParameters.token,
			    	}
	    		}
	    		
	    		if (objAjaxParameters.data !== undefined) {
	    			request.data = objAjaxParameters.data;
	    		}
	    	}
	    	
	    	$.ajax(request);
	    	return objStatus;
	    }
	
	    function validateEmail(email) {
	        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	        return re.test(email);
	    }
	
	    
	    // We will add functions to our library here !
	    return objOpenape;
	  }
	
	  // We need that our library is globally accesible, then we save in the window
	  if(typeof(window.openape) === 'undefined'){
	    window.openape = defineOpenape();
	  }
})(window);