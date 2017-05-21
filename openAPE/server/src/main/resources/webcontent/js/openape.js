(function(window){
	 function defineOpenape(){
	    var objOpenape = {};
	    
	    //get the protocol and adress of the location. If it´s running local, than the adress should be http://localhost:4567
	    var protocol = location.protocol;
	    
	    //
	    // FUNCTIONS FOR USER
	    //
	    
	    /** setUser
	     * 
	     * This function is used to set the user in the mongodb database with the given username, email and password
	     *
	     * @param  userName
	     * 	 The username of the user from the mongodb
	     * 
	     * @param  email
	     * 	 The email adress of the user from the mongodb
	     * 
	     * @param  password
	     * 	 The password of the user from the mongodb
	     * 
	     * @return      The function will send the user credentials to the function
	     * 				sendUserData and return a status as a boolean 
	     */
	    objOpenape.setUser = function (username, email, password) {
	   		var objUser = new Object();
   			var objSendUserdata = {};
	    	var objAjaxParameters = {};
	    	
	   		var arrRoles = [];
	   		
	   		var objErrResponse = new Object();
	   		var arrStatusTexts = [];
	   		
	   		var isUsernameCorrect = false;
	   		var isEmailCorrect = false;
	   		var isPasswordCorrect = false;
	    	
	   		arrRoles.push("admin");
	   		
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
		    	objAjaxParameters.url = protocol+"/users";
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
	    objOpenape.getUser = function (token) {
	    	var objUserProfile = {};
	    	var objAjaxParameters = {};
	    	
	    	var isTokenCorrect = true;
	    	
	    	if(token==""){
	    		objUserProfile.statusText = "Token can not be empty";
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		isTokenCorrect = false;
	    		objUserProfile.statusText = "Please enter a token";
	    	} 
	    	
	    	if(isTokenCorrect) {
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = protocol+"/profile";
	    		objAjaxParameters.token = token;
	    		objUserProfile = databaseCommunication(objAjaxParameters);
	    		
	    	} else {
	    		objUserProfile.status = 400;
	    	}
	    	
	    	return objUserProfile;
	    }
	    
	    /*
	    * TOKEN FUNCTIONS
	    */
	  
	    /** getToken
		* 
		* This function is used get the authorization token for the given grantTypem, username and password 
		*
		* @param  grant_type
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
	    objOpenape.getToken = function (grant_type, username, password) {
	    	var objToken = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isPasswordCorret = true;
	    	var isUsernameCorrect = true;
	    	var isGrantTypeCorrect = true;
	    	
	    	if(grant_type==""){
	    		arrStatusText.push("Grant type can not be empty");
	    		isGrantTypeCorrect = false;
	    	} else if (grant_type === undefined){
	    		arrStatusText.push("Please enter a grant type");
	    		isGrantTypeCorrect = false;
	    	} else if (grant_type != "password"){
	    		arrStatusText.push("Grant type should be password");
	    		isGrantTypeCorrect = false;
	    	}
	    	
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
	    	
	    	if(isPasswordCorret && isUsernameCorrect && isGrantTypeCorrect){
	    		objAjaxParameters.type = "POST";
	    		objAjaxParameters.url = protocol+"/token?grant_type="+grant_type+"&username="+username+"&password="+password;
	    		objToken = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objToken.statusText = arrStatusText;
	    		objToken.status = 400;
	    	}
	    	return objToken;
	    }
	    
	    /*
	     * USER-CONTEXTS FUNCTION
	     */
	   
	    /** getUserContexts
		* 
		* This function is used get the authorization token for the given grantTypem, username and password 
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all user contexts information
		*/
	    objOpenape.getUserContexts = function (token, userContextId) {
	    	var objGetUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
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
	    		objAjaxParameters.type = "GET";
	    		objAjaxParameters.url = protocol+"/api/user-contexts/"+userContextId;
	    		objAjaxParameters.token = token;
	    		objGetUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objGetUserContext_Result.status = 400;
	    		objGetUserContext_Result.statusText = arrStatusText;
	    	}
	    	
	    	return objGetUserContext_Result;
	    }
	    
	    /** setUserContexts
		* 
		* This function is used to set the user contexts  
		*
		* @param  userContexts
	    * 	 The given user contexts
		*
		* @param  token
		* 	 The authentification token to authorized the user
	    * 
		* @return      
		* 	 A javascript object with all status information of the set process
		*/	    
	    objOpenape.setUserContexts = function (token, userContexts) {
	    	var objSetUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContexts==""){
	    		arrStatusText.push("The usercontextId can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(userContexts === undefined){
	    		arrStatusText.push("Please enter a usercontextId");
	    		isUserContextCorrect = false;
	    	}
	    	
	    	if(isTokenCorrect && isUserContextCorrect){
	    		objAjaxParameters.data = userContexts;
	    		objAjaxParameters.type = "POST";
	    		objAjaxParameters.url = protocol+"/api/user-contexts";
	    		objAjaxParameters.token = token;
	    		objSetUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objSetUserContext_Result.statusText = arrStatusText;
	    		objSetUserContext_Result.status = 400;
	    	}
	    	return objSetUserContext_Result;
	    }
	    
	    /** deleteUserContexts
		* 
		* This function is used to delete user contexts  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @return      
		* 	 A javascript object with all status information of the delete process
		*/	    	    
	    objOpenape.deleteUserContexts = function (token, userContextId) {
	    	var objDeleteUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	
	    	var arrStatusText = [];
	    	
	    	var isTokenCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		objDeleteUserContext_Result.statusText = arrStatusText;
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
	    		objAjaxParameters.url = protocol+"/api/user-contexts/"+userContextId,
	    		objAjaxParameters.token = token;
	    		objDeleteUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objDeleteUserContext_Result.status = 400;
	    	}
	    	return objDeleteUserContext_Result;
	    }
	    
	    /** updateUserContexts
		* 
		* This function is used to update user contexts by the given usercontext id  
		*
		* @param  token
		* 	 The authentification token to authorized the user
		* 	 
	    * @param  userContextId
	    * 	 The stored userContextsId from mongodb
	    * 
		* @param  userContexts
	    * 	 The given user contexts
	    * 
		* @return      
		* 	 A javascript object with all status information of the update process
		*/	
	    objOpenape.updateUserContexts = function (token, userContextId, userContexts) {
	    	var objUpdateUserContext_Result = {};
	    	var objAjaxParameters = {};
	    	var arrStatusText = [];
	    	var isTokenCorrect = true;
	    	var isUserContextCorrect = true;
	    	var isuserContextIdCorrect = true;
	    	
	    	if(token==""){
	    		arrStatusText.push("The token can not be empty");
	    		isTokenCorrect = false;
	    	} else if(token === undefined){
	    		arrStatusText.push("Please enter a token");
	    		isTokenCorrect = false;
	    	}
	    	
	    	if(userContexts==""){
	    		arrStatusText.push("The usercontext can not be empty");
	    		isUserContextCorrect = false;
	    	} else if(userContexts === undefined){
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
	    		objAjaxParameters.data = userContexts;
	    		objAjaxParameters.type = "PUT";
	    		objAjaxParameters.url = protocol+"/api/user-contexts/"+userContextId;
	    		objAjaxParameters.token = token;
	    		objUpdateUserContext_Result = databaseCommunication(objAjaxParameters);
	    	} else {
	    		objUpdateUserContext_Result.status = 400;
	    		objUpdateUserContext_Result.statusText = arrStatusText;
	    	}
	    	return objUpdateUserContext_Result;
	    }     
	    
	    function databaseCommunication (objAjaxParameters) {
	    	var objStatus = {};
	    	var request =  
	    	{
	    		async: false,
		    	contentType: 'application/json',
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
	    		
	    		if (objAjaxParameters.token !== undefined) {
	    			request.headers = {
			    	   	 "Authorization": objAjaxParameters.token,
			    	}
	    		}
	    	} else if(objAjaxParameters.type == "POST"){
	    		request.type = objAjaxParameters.type;
	    		request.url = objAjaxParameters.url;
	    		
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
