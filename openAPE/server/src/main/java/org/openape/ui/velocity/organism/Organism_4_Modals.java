package org.openape.ui.velocity.organism;

import java.io.IOException;
import java.util.ArrayList;

import org.openape.api.user.User;
import org.openape.ui.velocity.molecules.Molecule_5_dataTableContent;
import org.openape.ui.velocity.requestHandler.AdminSectionRequestHandler;

public class Organism_4_Modals {
	
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



