package org.openape.ui.velocity.organism;

public class Organism_4_Modals {
	public String generateDeleteModal(String destination){
		
		String idName = "";
		if(destination.contains("Context")){
			idName = destination.replace("-", "");
		} else {
			idName = destination;
		}

		String modalContruct = "" 
			+ "<div class='modal fade' id='delete"+idName+"Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Delete "+destination+"</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
			+"       <h4>Do you realy want to delete the "+destination+" ?</h4>"
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmDelete"+idName+"'>Delete "+destination+"</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
	}

	public String generateAddContextModal(String contextName){
		String idName = "";
		if(contextName.contains("Context")){
			idName = contextName.replace("-", "");
		} else {
			idName = contextName;
		}
		
		String modalContruct = "" 
			+ "<div class='modal fade' id='add"+idName+"Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Add "+contextName+"</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
			+"      Add your Context in JSON-Format <textarea id='inputAdministrationAdd"+idName+"' class='form-control' rows='10' id='comment'></textarea>"
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='add"+idName+"MainErrSection'> </span>"	
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmAdd"+idName+"'>Add "+contextName+"</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
		}

	public String generateEditContextModal(String contextName){
		String idName = "";
		if(contextName.contains("Context")){
			idName = contextName.replace("-", "");
		} else {
			idName = contextName;
		}
		
		String modalContruct = "" 
			+ "<div class='modal fade' id='edit"+idName+"Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Edit "+contextName+"</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
			+"      Add your Context in JSON-Format <textarea id='inputAdministrationEdit"+idName+"' class='form-control' rows='10' id='comment'></textarea>"
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='edit"+idName+"MainErrSection'> </span>"	
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmEdit"+idName+"'>Edit "+contextName+"</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
		}

	public String generateAddGroupModal(){
		
		
		String modalContruct = "" 
			+ "<div class='modal fade' id='addGroupModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Add Group </h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em'>"
			+"		<table style='width:80%'><tr><td style='padding:10px'>Group name"
			+"  	<input type='text' class='form-control' id='inputSignupUsername' placeholder='Username'></td></tr><tr>"	
			+"      <td style='padding:10px'>User <textarea id='inputAdministrationAddGroup' class='form-control' rows='10' id='comment'></textarea>"
			+"     </td></tr></table>"	
			+"      </div>"
			
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='addGroupMainErrSection'> </span>"	
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmAddGroup'>Add Group</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
		}

	public String generateEditGroupModal(){
		
		String modalContruct = "" 
			+ "<div class='modal fade' id='editGroupModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>edit Group </h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em'>"
			+"		<table style='width:80%'><tr><td style='padding:10px'>Group name"
			+"  	<input type='text' class='form-control' id='inputGroupName' placeholder='Username'></td></tr><tr>"	
			+"      <td style='padding:10px'>User <textarea id='inputAdministrationEditGroup' class='form-control' rows='10' id='comment'></textarea>"
			+"     </td></tr></table>"	
			+"      </div>"
			
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='addGroupMainErrSection'> </span>"	
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmEditGroup'>Edit Group</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
		}
	
	public String generateSigninModal(){
		
		String modalSignin = "" 
			+ "<div class='modal fade' id='signinModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Sign in</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em'>"	
			+" <table width='100%' style='border-collapse:separate; border-spacing:1em;'>"
			+" <tr>"
        	+"	<td>Username: </td>"
        	+"	<td> "
	     	+" 		<div id='formGroupSigninUsername' class='form-group' role='form'>"
	     	+"			    <div class='col-sm-15'>"
	     	+"			      <input type='text' class='form-control' id='inputSigninUsername' placeholder='Username'>"
	     	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signinUsernameErrIcon'></span>"
	     	+"			     <small id='signinUsernameErrMessage' class='form-text text-muted' style='color:red'></small>"
	     	+"			    </div>"
			+"		 </div>"
        	+"	</td>"
        	+"</tr>"
        	+"<tr>"
        	+"	<td>Password: </td>"
        	+"	<td> "
        	+"		<div id='formGroupSigninPassword' class='form-group' role='form'>"
        	+"			 <div class='col-sm-15'>"
        	+"			      <input type='password' class='form-control' id='inputSigninPassword' placeholder='Password'>"
        	+"			       <small id='signinPasswordErrMessage' class='form-text text-muted' style='color:red'></small>"
        	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signPasswordErrIcon'></span>"
        	+"			    </div>"
			+"		 	 </div>"
        	+"	</td>"
        	+"</tr>"
        	+"<tr>"
        	+"	<td>Security Question (12+3)? </td>"
        	+"	<td> "
        	+"		<div id='formGroupSigninSecQuestion' class='form-group ' role='form'>"
        	+"			    <div class='col-sm-15'>"
        	+"			      <input type='text' class='form-control' id='inputSigninSecQuestion' placeholder='Security question'>"
        	+"			       <small id='signinSecQuestionErrMessage' class='form-text text-muted' style='color:red'></small>"
        	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signinSecQuestionErrIcon'></span>"
        	+"			    </div>"
			+"		 	 </div>"
        	+"	</td>" 
        	+"</tr> "
        	+"</table> "
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='signinMainErrSection'> </span>"		
			+"       <span style='float:right' ><button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"       <button type='button' class='btn btn-danger' id='btnConfirmSignin'>Sign in</button>"
			+"      </span></div>"
			+"</div>"
			+"</div>"
			+"</div>";
		
					
			return modalSignin;
		}
	
	public String generateSignupModal(){	
		String modalSignup = "" 
			+ "<div class='modal fade' id='signupModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Sign up</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em'>"	
			+" <table width='100%' style='border-collapse:separate; border-spacing:1em;'>"
			+" <tr>"
        	+"	<td>Username: </td>"
        	+"	<td> "
	     	+" 		<div id='formGroupSignupUsername' class='form-group' role='form'>"
	     	+"			    <div class='col-sm-15'>"
	     	+"			      <input type='text' class='form-control' id='inputSignupUsername' placeholder='Username'>"
	     	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupUsernameErrIcon'></span>"
	     	+"			     <small id='signupUsernameErrMessage' class='form-text text-muted' style='color:red'></small>"
	     	+"			    </div>"
			+"		 </div>"
        	+"	</td>"
        	+"</tr>"
        	+"<tr>"
        	+"	<td>Email: </td>"
        	+"	<td> "
        	+"		<div id='formGroupSignupEmail' class='form-group' role='form'>"
        	+"			 <div class='col-sm-15'>"
        	+"			      <input type='text' class='form-control' id='inputSignupEmail' placeholder='Email'>"
        	+"			       <small id='signupEmailErrMessage' class='form-text text-muted' style='color:red'></small>"
        	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupEmailErrIcon'></span>"
        	+"			    </div>"
			+"		 	 </div>"
        	+"	</td>"
        	+"</tr>"
        	+"<tr>"
        	+"	<td>Password: </td>"
        	+"	<td> "
        	+"		<div id='formGroupSignupPassword' class='form-group' role='form'>"
        	+"			 <div class='col-sm-15'>"
        	+"			      <input type='password' class='form-control' id='inputSignupPassword' placeholder='Password'>"
        	+"			       <small id='signupPasswordErrMessage' class='form-text text-muted' style='color:red'></small>"
        	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupPasswordErrIcon'></span>"
        	+"			    </div>"
			+"		 	 </div>"
        	+"	</td>"
        	+"</tr>"
        	+"<tr>"
        	+"	<td>Security Question (12+3)? </td>"
        	+"	<td> "
        	+"		<div id='formGroupSignupSecQuestion' class='form-group ' role='form'>"
        	+"			    <div class='col-sm-15'>"
        	+"			      <input type='text' class='form-control' id='inputSignupSecQuestion' placeholder='Security question'>"
        	+"			       <small id='signupSecQuestionErrMessage' class='form-text text-muted' style='color:red'></small>"
        	+"			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupSecQuestionErrIcon'></span>"
        	+"			    </div>"
			+"		 	 </div>"
        	+"	</td>" 
        	+"</tr> "
        	+"</table> "
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='signupMainErrSection'> </span>"		
			+"      <span style='float:right' > <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmSignup'>Sign up</button>"
			+"      </span></div>"
			+"</div>"
			+"</div>"
			+"</div>";
		
					
			return modalSignup;
		}
	
	
	
}



