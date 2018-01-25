/**
 * @version 1.0.2
 * @param window
 * @returns
 */

(function(window){
	 function defineOpenape(){
	    var objOpenape = {};

	    //get the protocol and address of the location. If it´s running local, than the address should be http://localhost:4567
	    
	    var token = "";
    
	    objOpenape.userContextsPath = "/api/user-contexts";	   
 objOpenape.taskContextPath = "/api/task-contexts";
 objOpenape.equipmentContextPath = "/api/equipment-contexts"
	    objOpenape.environmentContextPath = "/api/envronment-contexts";
	      
		    /** initializeLibrary
			* 
			* This function is used to get the authorization token for the given username and password. 
   *It returns a Java Script Object with all information related to a token (access_token, expires_in)
   * writes the access_token with the key "token" to the localstorage
			*
		    * @param{String}   userName - The openAPE username of the user 
					    * @param {String} password - The OpenAPE password of the user 
		    * @param {string} [serverURL=http://openape.gpii.eu] - the URL of the OpenAPE server to which the client shall connect. 
			* @returns {AccessToken} - A javascript object with all token information
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
	    
	    /** createUser
	     * 
	     * This function is used to create a new OpenAPE account with the given username, email and password
	     *
	     * @param  {String} - userName - The username of the user 
	     * @param  {string} email - The email address of the user 
	     * @param  {string} password - The password of the user 
	     *@param {string} [serverURL=http://openape.gpii.eu]- URL of the server to which the client connects.  
	     * @return {boolean}

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
	    * This function is used to get the account information of the user who initialized the client 
	    * 
	    * @return {object} -A javascript object with all found user information like username, email and roles
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
		    * This function is used to get user account information about a user.
		    *Can only be used if the user who initialized the client has Admin rights
		    * 
		    * @return {object} - A javascript object with all found user information like username, email and roles
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
		    
	       /** createUserContext
			* 
			* This function is used to upload a user context object to the OpenAPE server and to associate it with an Id.
					   		   	   			* This Function relates to ISO/IEC 24752-8 7.2.2 
			*
			* @param {UserContext} UserContext -	The user context that shall be uploaded
			*@param{string} contentType - the used content-type   
		*			 @return {object} - A javascript object with all status information of the create process
			*/	      
	     objOpenape.createUserContext = function (UserContext, contentType) {
		 return getContext(userContextPath, userContext, contentType);
	     }

	     /** getUserContext
			* 
			* This function can be used to retrieve a certain user context from the OpenAPE server with a given Id
			* It relates to ISO/ICE 24752-8 7.2.3
			*
			* 	 	     @param  {string} userContextId - The Id of the stored UserContext that shall be retrieved 
		    *@param {String} outputType - defines the dataformat of the received user context object. Can either be JSON or XML  
			* @return {Object} - A javascript object with all user contexts information
			*/
		     objOpenape.getUserContext = function (userContextId, outputType) {
	return getContext(objOpenape.userContextsPath,userContextId, outputType);
	}
     
		    /** updateUserContext
			* 
			* This function is used to update a certain user context on the OpenAPE server. It replaces a old one with a new one.
			* This Function relates to ISO/IEC 24752-8 7.2.4  
			* 	 
		    * @param  {string} userContextId - The Id of the user context that shall be replaced
		    * 
			* @param  {UserContext} UserContext - The new user context that shall replace the old one
		    *@param{string} contentType - the used content-type 
			* @return {object} - A javascript object with all status information of the update process
			*/	
		    objOpenape.updateUserContext = function (userContextId, UserContext, contentType) {
		    return updateContext(userContextPath, userContextId, contentType);	
		    }

		    /** deleteUserContext
			* 
			* This function is used to delete a certain user context
			* This Function relates to ISO/IEC 24752-8 7.2.5
						*
			* @param  {string} userContextId - The stored UserContextId that shall be deleted
		    * 
			* @return {object} - A javascript object with all status information of the delete process
			*/	    	    
		    objOpenape.deleteUserContext = function (userContextId) {
		    	return deleteContext(userContextPath, userContextId);
		    }

		    /** getUserContextList
			* 
			* This function is used to retrieve a list of URIs to accessible user contexts
			* This Function relates to ISO/IEC 24752-8 7.2.6
						*
			* @param  {string} userContextId - The stored UserContextId that shall be deleted
		    *
			* @param  {string} query - the query to filter the relevant contexts
				    *@param{string} contentType - the used content-type 
					*@return {object} - A javascript object with all status information
					*/
		    objOpenape.getUserContextList = function (query, contentType) {
return getContextList(userContextPath, query, contentType);
		    }

		    
		    
		    /** Task-Contexts Functions*/
		    
		    /** createTaskContext
			* 
			* This function is used to upload a task context object to the OpenAPE server and to associate it with an Id.
			*   This Function relates to ISO/IEC 24752-8 7.2.3 
						* @param  {TaskContext} taskContext -	The task context that shall be uploaded
						* @param{string} contentType - the used content-type 
			* @return {object} - A javascript object with all status information of the create process
			*/	    
		    	    objOpenape.createTaskContext = function (taskContext, contentType) {
return createTaskContext(taskContextPath, taskContextId, contentType);		    	    	
		    	    }

		    	    /** getTaskContext
		    	     * 		 
		    		* This function can be used to retrieve a certain taskr context from the OpenAPE server with a given Id
		    		* It relates to ISO/ICE 24752-8 7.3.3
		    		*
		    		* 	 	     @param  {string} taskContextId - The Id of the stored taskContext that shall be retrieved 
		    	    *@param {String} contentType - defines the dataformat of the received task context object. Can either be JSON or XML  
		    		* @return {Object} - A javascript object with all task context information
		    		*/

		    	    objOpenape.getTaskContext = function (taskContextId, contentType) {
		    	    	return getContext(taskContextPath, taskContextId, contentType);
		    	    }
		    	    
		    	    /** updateTaskContext
		    		* 
		    		* This function is used to update a certain task context on the OpenAPE server. It replaces a old one with a new one.
		    		* This Function relates to ISO/IEC 24752-8 7.3.4  
		    		* 	 
		    	    * @param  {string} taskContextId - The Id of the task context that shall be replaced
		    	    * 
		    		* @param  {TaskContext} taskContext - The new task context that shall replace the old one
		    	    * @param{string} contentType - the used content-type
		    		* @return {object} - A javascript object with all status information of the update process
		    		*/	
		    	    objOpenape.updateTaskContext = function (taskContextId, taskContext, contentType) {
return updateContext(taskContextPath, taskContextId, contentType);
		    	    }
		    	    
		    	    
		    	    
		    	    /** deleteTaskContext
		    	     * 
		    	     * 		 This function is used to delete a certain task context on the server
		    		* This Function relates to ISO/IEC 24752-8 7.3.5
		    				    		*
		    		* @param  {string} taskContextId - The Id of the task context that shall be deleted on the server 
		    	    * 
		    		* @return {object} - A javascript object with all status information of the delete process
		    		*/	    	    

		    	    objOpenape.deleteTaskContext = function (taskContextId) {
		    	    	return deleteContext(taskContextPath, taskContextId); 
		    	    }
		    	    
		    	    /** getTaskContextList
					* 
					* This function is used to retrieve a list of URIs to accessible task contexts
					* This Function relates to ISO/IEC 24752-8 7.3.6
								*
					* @param  {string} query - the query to filter the relevant contexts
				    *@param{string} contentType - the used content-type 
					* @return {object} - A javascript object with all status information 
					*/	    	    
				    objOpenape.getTaskContextList = function (query, contentType) {
		return getContextList(taskContextPath, query, contentType);
				    }


		    	    /*Equipment-Contexts functions*/

		    	    /** getEquipmentContext
		    		* 
		    		* This function can be used to retrieve a certain equipment context from the OpenAPE server with a given Id
		    		* It relates to ISO/ICE 24752-8 7.4.3
		    		*
		    		* 	 	     @param  {string} equipmentContextId - The Id of the stored equipmentContext that shall be retrieved 
		    	    *@param {String} outputType - defines the dataformat of the received equipment context object. Can either be JSON or XML  
		    		* @return {Object} - A javascript object with all equipment context information
		    		*/
		    	    objOpenape.getEquipmentContext = function (equipmentContextId, outputType) {
		    	    	return getContext(equipmentContextPath, equipmentContextId, outputType);
		    	    }

		    	    /** updateEquipmentContext
		    		* 
		    		* This function is used to update a certain equipment context on the OpenAPE server. It replaces a old one with a new one.
		    		* This Function relates to ISO/IEC 24752-8 7.4.4  
		    		* 	 
		    	    * @param  {string} EquipmentContextId - The Id of the equipment context that shall be replaced
		    	    		* @param  {EquipmentContext} equipmentContext - The new equipment context that shall replace the old one
		    	    *@param{string} contentType - the used content-type 
		    		* @return {object} - A javascript object with all status information of the update process
		    		*/	
		    	    objOpenape.updateEquipmentContext = function (equipmentContextId, equipmentContext, contentType) {
updateContext(equipmentContextPath, equipmentContextId, equipmentContext, contentType);
		    	    
		    	    /** deleteEquipmentContext
		    	     * 
		    	     * 		 This function is used to delete a certain equipment context on the server
		    		* This Function relates to ISO/IEC 24752-8 7.4.5
		    				    		*
		    		* @param  {string} equipmentContextId - The Id of the equipment context that shall be deleted on the server 
		    	    * 
		    		* @return {object} - A javascript object with all status information of the delete process
		    		*/	    	    
		    	    objOpenape.deleteEquipmentContext = function (equipmentContextId) {
		    	    	return deleteContext(equipmentContextPath, equipmentContextId );
		    	    }

		    	    /** getEquipmentContextList
					* 
					* This function is used to retrieve a list of URIs to accessible equipment contexts
					* This Function relates to ISO/IEC 24752-8 7.4.6
								*
					* @param  {string} query - the query to filter the relevant contexts
				    *@param{string} contentType - the used content-type 
					* @return {object} - A javascript object with all status information 
					*/	    	    
				    objOpenape.getEquipmentContextList = function (query, contentType) {
		return getContextList(equipmentContextPath, query, contentType);
				    }

				     * EnvironmentContext functions*/

		    /** createEnvironmentContext
			* 
			* This function is used to upload a environment context object to the OpenAPE server and to associate it with an Id.
			*   This Function relates to ISO/IEC 24752-8 7.5.2 
			*
			* @param  {EnvironmentContext} environmentContext -	The environment context that shall be uploaded 
			*
			* @return {object} - A javascript object with all status information of the create process
			*/	    
		    	    objOpenape.createEnvironmentContext = function (environmentContext, contentType) {
		    	    	return createContext(objOpenape.environmentContextPath, environmentContext, contentType);
		    	    }

					    /** getEnvironmentContext
						* 
						* This function can be used to retrieve a certain environment context from the OpenAPE server with a given Id
						* It relates to ISO/ICE 24752-8 7.5.3
						*
						* 	 	     @param  {string} environmentContextId - The Id of the stored environmentContext that shall be retrieved 
					    *@param {String} outputType - defines the dataformat of the received environment context object. Can either be JSON or XML  
						* @return {Object} - A javascript object with all environment context information
						*/
					    objOpenape.getEnvironmentContext = function (environmentContextId, outputType) {
					    return getContext(environmentContextPath, environmentContextId, outputType);	
					    }
					    }

				     
	     	    /** updateEnvironmentContext
		* 
		* This function is used to update a certain environment context on the OpenAPE server. It replaces a old one with a new one.
		* This Function relates to ISO/IEC 24752-8 7.5.4  
		* 	 
	    * @param  {string} environmentContextId - The Id of the environment context that shall be replaced
	    * 
		* @param  {EnvironmentContext} environmentContext - The new environment context that shall replace the old one
	    * 
		* @return {object} - A javascript object with all status information of the update process
		*/	
	    objOpenape.updateEnvironmentContext = function (ContextId, environmentContext, contentType) {
return updateContext(objOpenape.environmentContextPath, environmentContextId, environmentContext, contentType); 
}
	    
	    /** deleteEnvironmentContext
	     * 
	     * 		 This function is used to delete a certain environment context on the server
		* This Function relates to ISO/IEC 24752-8 7.5.5
				*
		* @param  {string} environmentContextId - The Id of the environment context that shall be deleted on the server 
	    * 
		* @return {object} - A javascript object with all status information of the delete process
		*/	    	    
	    objOpenape.deleteEnvironmentContext = function (environmentContextId) {
	    deleteContext(path, environmentContextId);
	    }

	    /** getEnvironmentContextList
		* 
		* This function is used to retrieve a list of URIs to accessible environment contexts
		* This Function relates to ISO/IEC 24752-8 7.5.6
					*
		* @param  {string} query - the query to filter the relevant contexts
	    *@param{string} contentType - the used content-type 
		* @return {object} - A javascript object with all status information 
		*/	    	    
	    objOpenape.getEnvironmentContextList = function (query, contentType) {
return getContextList(environmentContextPath, query, contentType);
	    }

	    
	    	   var  getContext = function (path, contextId, outputType) {
	    	let objGetContext_Result = {};
	    	let objAjaxParameters = {};
	    	localStorage.setItem("host", "http://"+window.location.host);
	    	
	    	let arrStatusText = [];
	    	let isTokenCorrect = true;
	    	let isContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(contextId==""){
	    		arrStatusText.push("The contextId can not be empty");
	    		isContextIdCorrect = false;
	    	} else if(userContextId === undefined){
	    		arrStatusText.push("Please enter a contextId");
	    		isContextIdCorrect = false;
	    	}
	    	  	if(isTokenCorrect && isContextIdCorrect){
	    		if(outputType == "JSON"){
	    		objajaxParameters.contentType = "application/json";
	    		} else {
	    			objAjaxParameters.contentType = "application/xml";
	    		}
objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = localStorage.getItem("host")+ path+userContextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objGetContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
alert("error");
	    	};
	    	   }
    	    let createContext = function (path, environmentContext, contentType) {
    	    	let context_Result = {};
    	    	let objAjaxParameters = {};
    	    	
    	    	let arrStatusText = [];
    	    	let isTokenCorrect = true;
    	    	let  isEnvironmentContextCorrect = true;
    	    	
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
    	    };;
    	    

var updateContext = function (contextId, Context, contentType) {
	    	var objUpdateContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isContextCorrect = true;
	    	var isContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library");
	    		isTokenCorrect = false;
	    	} 
	    	
	    	if(context==""){
	    		arrStatusText.push("The context can not be empty");
	    		isContextCorrect = false;
	    	} else if(context === undefined){
	    		arrStatusText.push("Please enter a context");
	    		isContextCorrect = false;
	    	}
	    	
	    	if(contextId==""){
	    		arrStatusText.push("The contextId can not be empty");
	    		isContextIdCorrect = false;
	    	} else if(contextId === undefined){
	    		arrStatusText.push("Please enter a contextId");
	    		isContextIdCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isContextCorrect && isContextIdCorrect ){
	    		objAjaxParameters.data = context;
	    		objAjaxParameters.type = "PUT";
	    		
	    		switch (contentType){
					case "JSON" : objAjaxParameters.contentType = 'application/json'; break;
					case "XML" : objAjaxParameters.contentType = 'application/xml';break;	
	    		};
	    	}
	    		
	    		let deleteContext = function (contextId) {
	    	    	let objDeleteContext_Result = {};
	    	    	let objAjaxParameters = {};
	    	    	
	    	    	let arrStatusText = [];
	    	    	
	    	    	let isTokenCorrect = true;
	    	    	let isContextIdCorrect = true;
	    	    	
	    	    	if(localStorage.getItem("token") === undefined){
	    	    		arrStatusText.push("Please initialize the library");
	    	    		isTokenCorrect = false;
	    	    	} 
	    	    	
	    	    	if(environmentContextId==""){
	    	    		arrStatusText.push("The contextId can not be empty");
	    	    		isEnvironmentContextIdCorrect = false;
	    	    	} else if(contextId === undefined){
	    	    		arrStatusText.push("Please enter a ContextId");
	    	    		isContextIdCorrect = false;
	    	    	}
	    	    	
	    	    	if(isTokenCorrect && isContextIdCorrect ){
	    	    		objAjaxParameters.type = "DELETE";
	    	    		objAjaxParameters.url = localStorage.getItem("host")path;
	    	    		objAjaxParameters.token = localStorage.getItem("token");
	    	    		objDeleteContext_Result = databaseCommunication(objAjaxParameters);
	    	    	} else {
	    	    		objDeleteContext_Result.statusText = arrStatusText;
	    	    		objDeleteContext_Result.status = 400;
	    	    	}
	    	    	return objDeleteContext_Result;
	    	    }

	    		
	    		objAjaxParameters.url = localStorage.getItem("host")+path+contextId;
	    		objAjaxParameters.token = localStorage.getItem("token");
	    		objUpdatecontext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdatecontext_Result.status = 400;
	    		objUpdatecontext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdatecontext_Result;
	    }; 
	    
	    function getContextList(query, contentType){
	    	
	    }
	    




	    		/**  Executes  Ajax requests
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
	        var re = /^(([^<>()[]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
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
	   