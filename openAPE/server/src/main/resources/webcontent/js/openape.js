/**
 * @version 1.0.4
 * @param window
 * @returns
 */

(function(window){
	function defineOpenape(){
		var objOpenape = {};

	    // get the protocol and address of the location. If it´s running local,
		// than the address should be http://localhost:4567
	    
	    var token = "";
	    objOpenape.defaultContentType = "application/json";    
	    objOpenape.userContextPath = "/api/user-contexts";	   
	    objOpenape.taskContextPath = "/api/task-contexts";
	    objOpenape.equipmentContextPath = "/api/equipment-contexts"
	    objOpenape.environmentContextPath = "/api/environment-contexts";
	      
	    /**
		 * initializeLibrary
		 * 
		 * This function is used to get the authorization token for the
		 * given username and password. It returns a Java Script Object with
		 * all information related to a token (access_token, expires_in)
		 * writes the access_token with the key "token" to the localstorage
		 * 
		 * @param{String} userName - The openAPE username of the user
		 * @param {String}
		 *            password - The OpenAPE password of the user
		 * @param {string}
		 *            [serverURL=http://openape.gpii.eu] - the URL of the
		 *            OpenAPE server to which the client shall connect.
		 * @returns {AccessToken} - A javascript object with all token
		 *          information
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
		    
		   	var isPasswordCorrect = true;
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
		   		isPasswordCorrect = false;
		   	} else if(password === undefined){
		   		arrStatusText.push("Please enter a password");
		   		isPasswordCorrect = false;
		   	}
		   	
		   	if(isPasswordCorrect && isUsernameCorrect){
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
	    
	    /**
		 * createUser
		 * 
		 * This function is used to create a new OpenAPE account with the given
		 * username, email and password
		 * 
		 * @param {String} -
		 *            userName - The username of the user
		 * @param {string}
		 *            email - The email address of the user
		 * @param {string}
		 *            password - The password of the user
		 * @param {string}
		 *            [serverURL=http://openape.gpii.eu]- URL of the server to
		 *            which the client connects.
		 * @return {boolean}
		 * 
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
	   		
	   		// check if username is correct
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
	    		
	   		// check if email is correct
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
	    		
	   		// check if password is correct
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

	    /**
		 * getUser
		 * 
		 * This function is used to get the account information of the user who
		 * initialized the client
		 * 
		 * @return {object} -A javascript object with all found user information
		 *         like username, email and roles
		 */
	    objOpenape.getUser = function () {
	    	var objUserProfile = {};
	    	var objAjaxParameters = {};
	    	
	    	if(isTokenCorrect()) {
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/profile";
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUserProfile = databaseCommunication(objAjaxParameters);
	    		
	    	} else {
	    		objUserProfile.status = 400;
	    	}
	    	
	    	return objUserProfile;
	    }
	    
	    
	    /**
		 * getUserById
		 * 
		 * This function is used to get user account information about a user.
		 * Can only be used if the user who initialized the client has Admin
		 * rights
		 * 
		 * @return {object} - A javascript object with all found user
		 *         information like username, email and roles
		 */
	    objOpenape.getUserById = function (userId) {
	    	var objUserProfile = {};
	    	var objAjaxParameters = {};
	    	
	    	if(isTokenCorrect()) {
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+"/users/openape/"+userId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUserProfile = databaseCommunication(objAjaxParameters);
	    		
	    	} else {
	    		objUserProfile.status = 400;
	    	}
	    	
	    	return objUserProfile;
	    }
		    
       /**
        * createUserContext
		* 
		* This function is used to upload a user context object to the
		* OpenAPE server and to associate it with an Id. This Function
		* relates to ISO/IEC 24752-8 7.2.2
		* 
		* @param {UserContext}
		*            UserContext - The user context that shall be uploaded
		* @param{string} contentType - the used content-type
		* @return {object} - A javascript object with all status
		*         information of the create process
		*/	      
	   objOpenape.createUserContext = function (userContext, contentType) {
		   return createContext(objOpenape.userContextPath, userContext, contentType);
	   }

	   /**
		* getUserContext
		* 
		* This function can be used to retrieve a certain user context from
		* the OpenAPE server with a given Id It relates to ISO/ICE 24752-8
		* 7.2.3
		* 
		* @param {string}
		*            userContextId - The Id of the stored UserContext that
		*            shall be retrieved
		* @param {String}
		*            outputType - defines the dataformat of the received
		*            user context object. Can either be JSON or XML
		* @return {Object} - A javascript object with all user contexts
		*         information
		*/
	   objOpenape.getUserContext = function (userContextId, outputType) {
		   return getContext(objOpenape.userContextsPath,userContextId, outputType);
	   }
     
	   /**
	    * updateUserContext
	    * 
	    * This function is used to update a certain user context on the
	    * OpenAPE server. It replaces a old one with a new one. This
	    * Function relates to ISO/IEC 24752-8 7.2.4
	    * 
	    * @param {string}
	    *            userContextId - The Id of the user context that shall
	    *            be replaced
	    * 
	    * @param {UserContext}
	    *            UserContext - The new user context that shall replace
	    *            the old one
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information of the update process
	    */	
	   objOpenape.updateUserContext = function (userContextId, userContext, contentType) {
		   return updateContext(objOpenape.userContextPath, userContextId,  userContext, contentType);	
	   }
	   
	   /**
	    * deleteUserContext
	    * 
	    * This function is used to delete a certain user context This
	    * Function relates to ISO/IEC 24752-8 7.2.5
	    * 
	    * @param {string}
	    *            userContextId - The stored UserContextId that shall be
	    *            deleted
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the delete process
	    */	    	    
	   objOpenape.deleteUserContext = function (userContextId) {
		   return deleteContext(objOpenape.userContextPath, userContextId);
	   }
	   
	   /**
	    * getUserContextList
	    * 
	    * This function is used to retrieve a list of URIs to accessible
	    * user contexts This Function relates to ISO/IEC 24752-8 7.2.6
	    * 
	    * @param {string}
	    *            query - the query to filter the relevant contexts
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information
	    */
	   objOpenape.getUserContextList = function (query, contentType) {
		   return getContextList(objOpenape.userContextPath, query, contentType);
	   	}
	   
	   /* Task-Contexts Functions */
	   
	   /**
	    * createTaskContext
	    * 
	    * This function is used to upload a task context object to the
	    * OpenAPE server and to associate it with an Id. This Function
	    * relates to ISO/IEC 24752-8 7.2.3
	    * 
	    * @param {TaskContext}
	    *            taskContext - The task context that shall be uploaded
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information of the create process
	    */	    
	   objOpenape.createTaskContext = function (taskContext, contentType) {
		   return createContext(objOpenape.taskContextPath, taskContext, contentType);		    	    	
	   }
	   
	   /**
	    * getTaskContext
	    * 
	    * This function can be used to retrieve a certain taskr context
	    * from the OpenAPE server with a given Id It relates to ISO/ICE
	    * 24752-8 7.3.3
	    * 
	    * @param {string}
	    *            taskContextId - The Id of the stored taskContext
	    *            that shall be retrieved
	    * @param {String}
	    *            contentType - defines the dataformat of the
	    *            received task context object. Can either be JSON
	    *            or XML
	    * @return {Object} - A javascript object with all task context
	    *         information
	    */
	   objOpenape.getTaskContext = function (taskContextId, contentType) {
		   return getContext(objOpenape.taskContextPath, taskContextId, contentType);
	   }
	   
	   /**
	    * updateTaskContext
	    * 
	    * This function is used to update a certain task context on
	    * the OpenAPE server. It replaces a old one with a new one.
	    * This Function relates to ISO/IEC 24752-8 7.3.4
	    * 
	    * @param {string}
	    *            taskContextId - The Id of the task context
	    *            that shall be replaced
	    * 
	    * @param {TaskContext}
	    *            taskContext - The new task context that shall
	    *            replace the old one
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information of the update process
	    */	
	   objOpenape.updateTaskContext = function (taskContextId, taskContext, contentType) {
		   return updateContext(objOpenape.taskContextPath, taskContextId, taskContext, contentType);
	   }
	   
	   /**
	    * deleteTaskContext
	    * 
	    * This function is used to delete a certain task context on
	    * the server This Function relates to ISO/IEC 24752-8 7.3.5
	    * 
	    * @param {string}
	    *            taskContextId - The Id of the task context
	    *            that shall be deleted on the server
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the delete process
	    */	    	    
	   
	   objOpenape.deleteTaskContext = function (taskContextId) {
		   return deleteContext(objOpenape.taskContextPath, taskContextId); 
	   }
	   
	   /**
	    * getTaskContextList
	    * 
	    * This function is used to retrieve a list of URIs to
	    * accessible task contexts This Function relates to ISO/IEC
	    * 24752-8 7.3.6
	    * 
	    * @param {string}
	    *            query - the query to filter the relevant
	    *            contexts
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information
	    */	    	    
	   objOpenape.getTaskContextList = function (query, contentType) {
		   return getContextList(objOpenape.taskContextPath, query, contentType);
	   }
	   
	   
	   /* Equipment-Contexts functions */
	   /**
	    * createEquipmentContext
	    * 
	    * This function is used to upload a equipment context
	    * object to the OpenAPE server and to associate it with an
	    * Id. This Function relates to ISO/IEC 24752-8 7.4.2
	    * 
	    * @param {EquipmentContext}
	    *            equipmentContext - The equipment context that
	    *            shall be uploaded
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the create process
	    */	    
	   objOpenape.createEquipmentContext = function (equipmentContext, contentType) {
		   return createContext(objOpenape.equipmentContextPath, equipmentContext, contentType);
	   }
	   
	   
	   /**
	    * getEquipmentContext
	    * 
	    * This function can be used to retrieve a certain equipment
	    * context from the OpenAPE server with a given Id It
	    * relates to ISO/ICE 24752-8 7.4.3
	    * 
	    * @param {string}
	    *            equipmentContextId - The Id of the stored
	    *            equipmentContext that shall be retrieved
	    * @param {String}
	    *            outputType - defines the dataformat of the
	    *            received equipment context object. Can either
	    *            be JSON or XML
	    * @return {Object} - A javascript object with all equipment
	    *         context information
	    */
	   objOpenape.getEquipmentContext = function (equipmentContextId, outputType) {
		   return getContext(objOpenape.equipmentContextPath, equipmentContextId, outputType);
	   }
	   
	   /**
	    * updateEquipmentContext
	    * 
	    * This function is used to update a certain equipment
	    * context on the OpenAPE server. It replaces a old one with
	    * a new one. This Function relates to ISO/IEC 24752-8 7.4.4
	    * 
	    * @param {string}
	    *            EquipmentContextId - The Id of the equipment
	    *            context that shall be replaced
	    * @param {EquipmentContext}
	    *            equipmentContext - The new equipment context
	    *            that shall replace the old one
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information of the update process
	    */	
	   objOpenape.updateEquipmentContext = function (equipmentContextId, equipmentContext, contentType) {
		   return updateContext(objOpenape.equipmentContextPath, equipmentContextId, equipmentContext, contentType);
	   }
	   
	   /**
	    * deleteEquipmentContext
	    * 
	    * This function is used to delete a certain equipment
	    * context on the server This Function relates to ISO/IEC
	    * 24752-8 7.4.5
	    * 
	    * @param {string}
	    *            equipmentContextId - The Id of the equipment
	    *            context that shall be deleted on the server
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the delete process
	    */	    	    
	   objOpenape.deleteEquipmentContext = function (equipmentContextId) {
		   return deleteContext(objOpenape.equipmentContextPath, equipmentContextId );
	   }
		   	
	   /**
	    * getEquipmentContextList
	    * 
	    * This function is used to retrieve a list of URIs to
	    * accessible equipment contexts This Function relates to
	    * ISO/IEC 24752-8 7.4.6
	    * 
	    * @param {string}
	    *            query - the query to filter the relevant
	    *            contexts
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status
	    *         information
	    */	    	    
	   objOpenape.getEquipmentContextList = function (query, contentType) {
		   return getContextList(objOpenape.equipmentContextPath, query, contentType);
	   }
		   
	   /* EnvironmentContext functions */
		   
	   /**
	    * createEnvironmentContext
	    * 
	    * This function is used to upload a environment context
	    * object to the OpenAPE server and to associate it with an
	    * Id. This Function relates to ISO/IEC 24752-8 7.5.2
	    * 
	    * @param {EnvironmentContext}
	    *            environmentContext - The environment context
	    *            that shall be uploaded
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the create process
	    */
	   objOpenape.createEnvironmentContext = function (environmentContext, contentType) {
		   return createContext(objOpenape.environmentContextPath, environmentContext, contentType);
	   }
		    	    
	   /**
	    * getEnvironmentContext
	    * 
	    * This function can be used to retrieve a certain
	    * environment context from the OpenAPE server with a
	    * given Id It relates to ISO/ICE 24752-8 7.5.3
	    * 
	    * @param {string}
	    *            environmentContextId - The Id of the
	    *            stored environmentContext that shall be
	    *            retrieved
	    * @param {String}
	    *            outputType - defines the dataformat of the
	    *            received environment context object. Can
	    *            either be JSON or XML
	    * @return {Object} - A javascript object with all
	    *         environment context information
	    */
	   objOpenape.getEnvironmentContext = function (environmentContextId, outputType) {
		   return getContext(objOpenape.environmentContextPath, environmentContextId, outputType);	
	   }
	   
	   /**
	    * updateEnvironmentContext
	    * 
	    * This function is used to update a certain environment context
	    * on the OpenAPE server. It replaces a old one with a new one.
	    * This Function relates to ISO/IEC 24752-8 7.5.4
	    * 
	    * @param {string}
	    *            environmentContextId - The Id of the environment
	    *            context that shall be replaced
	    * 
	    * @param {EnvironmentContext}
	    *            environmentContext - The new environment context
	    *            that shall replace the old one
	    * 
	    * @return {object} - A javascript object with all status
	    *         information of the update process
	    */	
	   objOpenape.updateEnvironmentContext = function (ContextId, environmentContext, contentType) {
		   return updateContext(objOpenape.environmentContextPath, environmentContextId, environmentContext, contentType); 
	   }	
	    
	   /**
	    * deleteEnvironmentContext
	    * 
	    * This function is used to delete a certain environment context on the
	    * server This Function relates to ISO/IEC 24752-8 7.5.5
	    * 
	    * @param {string}
	    *            environmentContextId - The Id of the environment context
	    *            that shall be deleted on the server
	    * 
	    * @return {object} - A javascript object with all status information of
	    *         the delete process
	    */	    	    
	   objOpenape.deleteEnvironmentContext = function (environmentContextId) {
		   return deleteContext(objOpenape.environmentContextPath, environmentContextId);
	   }
	   
	   /**
	    * getEnvironmentContextList
	    * 
	    * This function is used to retrieve a list of URIs to accessible
	    * environment contexts This Function relates to ISO/IEC 24752-8 7.5.6
	    * 
	    * @param {string}
	    *            query - the query to filter the relevant contexts
	    * @param{string} contentType - the used content-type
	    * @return {object} - A javascript object with all status information
	    */	    	    
	   objOpenape.getEnvironmentContextList = function (query, contentType) {
		   return getContextList(objOpenape.environmentContextPath, query, contentType);
	   } 
	   
	   var  getContext = function (path, contextId, outputType) {
		   let objGetContext_Result = {};
		   let arrStatusText = [];
	    	    	    	
		   if(isTokenCorrect() && isContextIdCorrect(contextId) ){
			   let objAjaxParameters = createAjaxObject("GET", path + "/" + contextId, outputType);
			   objGetContext_Result = databaseCommunication(objAjaxParameters);
		   } else {
			   alert("error");
			   return  obj
		   }
		   return objGetContext_Result;
	   };
	    	   
	   let createContext = function (path, context, contentType) {
		   let context_Result = {};
		   let arrStatusText = [];
    	    	
		   if(isTokenCorrect() && isContextCorrect(context)){	
			   objAjaxParameters = createAjaxObject("POST", path, contentType );
			   objAjaxParameters.data = context;
			   objcreateContext_Result = databaseCommunication(objAjaxParameters);
			   
			   
		   } else {
			   objcreateContext_Result.statusText = arrStatusText;
			   objcreateContext_Result.status = 400;
		   }
		   return objcreateContext_Result;
	   }
    	    
	   var updateContext = function (path, contextId, context, contentType) {
		   var objUpdateContext_Result = {};
		   
	    			   if(isTokenCorrect() && isContextCorrect(context) && isContextIdCorrect(contextId) ){
			   let objAjaxParameters = createAjaxObject("PUT", path+"/" +contextId, contentType); 
			   objAjaxParameters.data = context;
			   objUpdatecontext_Result = databaseCommunication(objAjaxParameters);
		   } else {
			   objUpdatecontext_Result.status = 400;
			   console.log("Could not send request");
			   objUpdatecontext_Result.statusText = arrStatusText;
		   }
		   return objUpdatecontext_Result;
	   }; 
	    	
	   /*
	    * Function to delete all kind of contexts
	    * 
	    */
	   let deleteContext = function(path, contextId) {
		   
		     var objDeleteContext_Result = {};
 let objAjaxParameters = createAjaxObject("DELETE", path + "/" + contextId);
 if(isTokenCorrect() && isContextIdCorrect(contextId) ){
	 objDeleteContext_Result = databaseCommunication(objAjaxParameters); 
} else {
     objDeleteContext_Result.statusText = "incorrect context id or token";
    objDeleteContext_Result.status = 400; } 
return 		     objDeleteContext_Result;
		    
	   }

	   function getContextList(path, query, contentType){
		   var ajaxParams = createAjaxObject("GET",path, contentType);
		   let response = databaseCommunication(ajaxParams);
		   
		   responseText = response.responseText;
		   
		   var result;
		   if (ajaxParams.contentType == "application/json"){
			   result = JSON.parse(responseText);
		   } else if (ajaxParams == "application/xml"){
			   result = XML.parse(responseText);
		   }
		   else {
			   result = JSON.parse(responseText);
		   }
	   }
	   
	   objOpenape.changePassword = function(oldPw, newPw) {
		   id = getUser().userId;
		   var ajaxParams = createAjaxObject("Put", "openape/users/id/password" );
		   var passwordChangeRequest = {};
		   passwordChangeRequest.oldPassword = oldPw;
		   passwordChangeRequest.newPassword = newPw;
		   ajaxParams.data = JSON.stringify(passwordChangeRequest);
		   var response = databaseCommunication(ajaxParams);
	   }
	   
	   
	   /**
	    * Executes Ajax requests
	    * 
	    * @returns JQXHR object
	    */
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

	   function isTokenCorrect(){
		   let isTokenCorrect = true;
		   if(localStorage.getItem("token") === undefined){
			   arrStatusText.push("Please initialize the library");
			   isTokenCorrect = false;
		   } 
		   return isTokenCorrect;
	   }	    	

	   function isContextIdCorrect(contextId){
		   let isContextIdCorrect = true;
		   if(contextId==""){
			   arrStatusText.push("The contextId can not be empty");
			   isContextIdCorrect = false;
		   } else if(contextId === undefined){
			   arrStatusText.push("Please enter a contextId");
			   isContextIdCorrect = false;
		   }
	    		    	
		   return isContextIdCorrect;
	   }
	    	
	   function isContextCorrect(context){
		   var isContextCorrect = true;
		   if(context==""){
			   arrStatusText.push("The context can not be empty");
			   isContextCorrect = false;
		   } else if(context === undefined){
			   arrStatusText.push("Please enter a context");
			   isContextCorrect = false;
		   }
		   return isContextCorrect;	    	
	   }
	    	
	   function createAjaxObject(verb, path, contentType){
		   let objAjaxParameters = {};
		   console.log("path: " + path);
		   if(contentType == "JSON"){
			   objAjaxParameters.contentType = "application/json";
		   } else if (contentType == "XML"){
			   objAjaxParameters.contentType == "application/xml";
		   } else {
			   			   objAjaxParameters.contentType = objOpenape.defaultContentType;	
		   }
		   
		   objAjaxParameters.type = verb;
		   objAjaxParameters.url = localStorage.getItem("host") + path;
		 
		   if (isTokenCorrect){
			   objAjaxParameters.token = localStorage.getItem("token");
		   }
		   return objAjaxParameters
	   }
    
	   function validateEmail(email) {	    
	       /*  
		   var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	        return re.test(email);
	        */
	   }
	
	    
	   
	   // We will add functions to our library here !


	   return objOpenape;
	}
	
	  // We need that our library is globally accesible, then we save in the
		// window
	if(typeof(window.openape) === 'undefined'){
		window.openape = defineOpenape();
	}
})(window);
