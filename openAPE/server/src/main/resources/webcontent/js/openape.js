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
    objOpenape.userContextsPath = "user-contexts";	   
 
	      
		    /** initializeLibrary
			* 
			* This function is used to get the authorization token for the given username and password. 
   *It returns a Java Script Object with all information related to a token (access_token, expires_in)
   * writes the access_token with the key "token" to the localstorage
			*
			* 	 
		    * @param{String}   userName - The openAPE username of the user 
					    * @param {String} password - The OpenAPE password of the user 
		    * @param {string} [serverURL=http://openape.gpii.eu] - the URL of the OpenAPE server to which the client shall connect. 
			* @returns {AccessToken} - A javascript object with all token information
			* */
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
	     * This function is used to create a new OpenAPE account with the given username, email and password
	     *
	     * @param  {String} - userName - The username of the user 
	     * @param  {string} email - The email address of the user 
	     * @param  {string} password - The password of the user 
	     *@param {string} [serverURL=http://openape.gpii.eu]
- URL of the server to which the client connects.  
	     * @return      
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
	   
	    	    /** getUserContext
		* 
		* This function can be used to retrieve a certain user context from the OpenAPE server with a given Id
		* It relates to ISO/ICE 24752-8 7.2.3
		*
		* 	 	     @param  {string} userContextId - The Id of the stored UserContext that shall be retrieved 
	    *@param {String} outputType - defines the dataformat of the received user context object. Can either be JSON or XML  
		* @return {Object} - A javascript object with all user contexts information
		*/
	    objOpenape.getUserContext = function (userContextId, outputType) \{
return getContext(objOpenape.userContextsPath,userContextId, outputType);
}

	    objOpenape.getContext = function (path, ContextId, outputType) {
	    	var objGetUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	localStorage.setItem("host", "http://"+window.location.host);
	    	
	    	var arrStatusText = []\;
	    	var isTokenCorrect = true;
	    	var isContextIdCorrect = true;
	    	
	    	if(localStorage.getItem("token") === undefined){
	    		arrStatusText.push("Please initialize the library")\;
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