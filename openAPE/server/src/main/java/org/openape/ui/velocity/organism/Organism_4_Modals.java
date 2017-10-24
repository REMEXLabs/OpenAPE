package org.openape.ui.velocity.organism;

public class Organism_4_Modals {
    public String generateContextModal(final String contextName, final String actionNameUpperCase) {
        final String actionNameLowerCase = actionNameUpperCase.substring(0, 1).toLowerCase()
                + actionNameUpperCase.substring(1);

        String idName = "";
        if (contextName.contains("Context")) {
            idName = contextName.replace("-", "");
        } else {
            idName = contextName;
        }

        final String modalContruct = "" + "<div class='modal fade' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 id='exampleModalLongTitle'>"
                + actionNameUpperCase
                + " "
                + contextName
                + "</h3>"
                + "      </div>"
                + "      <div class='modal-body' style='min-height:10em;padding-bottom: 4em; padding-left: 2em; padding-top: 2em'>"
                + "      Add your Context in JSON- or XML-Format <textarea style='width: 100% !important' id='inputAdministration"
                + actionNameUpperCase
                + ""
                + idName
                + "' class='form-control' rows='13' id='comment'></textarea>"
                + " <div style='margin-top:1em'>"
                + "	<div style='float:left; margin-right:3em;' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "divPublicCb'>"

                + "   <span style='margin-right:2.5em'><b>Public:</b></span> <span><input type='checkbox' id='cb"
                + actionNameUpperCase
                + ""
                + idName
                + "'  style='transform: scale(1.5)'></span>"
                + "</div><div style='float:left'> "
                + " <span style='margin-right: 2em;margin-left: 1.5em;' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "lbOutput'><b>Output:</b></span>"
                + "<select style='margin-right:5em' class='selectpicker' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "OutputSelContentType'>"
                + "<option>JSON</option>"
                + "<option>XML</option>"
                + "</select>"
                + "<span style='margin-right:2.5em'><b>Content-Type:</b></span> "
                + "<select class='selectpicker' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "SelContentType'>"
                + "<option>JSON</option>"
                + "<option>XML</option>"
                + "</select>"
                + "</div></div>"
                + "      </div>"
                + "      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
                + "		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='"
                + actionNameLowerCase
                + ""
                + idName
                + "MainErrSection'> </span>"
                + "       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
                + "        <button type='button' class='btn btn-danger' id='btnConfirm"
                + actionNameUpperCase + "" + idName + "'>" + actionNameUpperCase + " "
                + contextName + "</button>" + "      </div>" + "</div>" + "</div>" + "</div>";

        return modalContruct;
    }

    public String generateDeleteModal(final String destination) {

        String idName = "";
        if (destination.contains("Context")) {
            idName = destination.replace("-", "");
        } else {
            idName = destination;
        }

        final String modalContruct = "" + "<div class='modal fade' id='delete"
                + idName
                + "Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 style='color:white; margin-bottom:0; margin-top:0'>Delete "
                + destination
                + "</h3>"
                + "      </div>"
                + "      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
                + "       <h4>Do you realy want to delete the "
                + destination
                + " ?</h4>"
                + "      </div>"
                + "      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
                + "       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
                + "        <button type='button' class='btn btn-danger' id='btnConfirmDelete"
                + idName + "'>Delete " + destination + "</button>" + "      </div>" + "</div>"
                + "</div>" + "</div>";

        return modalContruct;
    }

    // -------------------GROUPS------------------
    public String generateGroupModal(final String action) {
        final String actionLowerCase = action.substring(0, 1).toLowerCase() + action.substring(1);

        final String modalContruct = "<div class='modal fade' id='"
                + actionLowerCase
                + "GroupModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 class='modal-title' id='exampleModalLongTitle'>"
                + action
                + " Group</h3>"
                + "      </div>"
                + "      <div class='modal-body'>"
                + "		<div class='form-group' id='"
                + actionLowerCase
                + "FormGoupName'>"
                + "			<label style='margin-top:1em'>Group name:</label>"
                + "			<input id='"
                + actionLowerCase
                + "GroupNameInput' class='form-control'>"
                + " 		</div>"
                + "		<div class='form-group' id='"
                + actionLowerCase
                + "FormGoupDescription'>"
                + "			<label style='margin-top:1em'>Group description:</label>"
                + "			<input id='"
                + actionLowerCase
                + "GroupDescriptionInput' class='form-control'>"
                + "		</div>"
                + "		<div class='form-group' id='"
                + actionLowerCase
                + "FormGoupDescription'>"
                + "   <span style='margin-right:2.5em'><b>Public:</b></span> <span><input type='checkbox' id='cb"
                + action
                + "OpenAcces' style='transform: scale(1.5)'></span>"
                + "		</div>"
                + "		</div>"
                + "      <div class='modal-footer' style='clear:both; background-color:#ececec; border-top: 2px solid #ccc;'>"
                + "	<span style='float:left;color:red;font-weight:bold;font-size:12pt' id='"
                + actionLowerCase
                + "GroupMainErrSection'> </span>"
                + "        <button type='button' class='btn btn-secondary' data-dismiss='modal'>Close</button>"
                + "        <button type='button' class='btn btn-danger' id='btnConfirm" + action
                + "Group' class='btn btn-primary'>" + action + " Group</button>" + "      </div>"
                + "    </div>" + "  </div>" + "</div>";

        return modalContruct;
    }

    public String generateGroupUserModal(final String action) {
        final String actionLowerCase = action.substring(0, 1).toLowerCase() + action.substring(1);

        final String modalContruct = "<div class='modal fade' id='"
                + actionLowerCase
                + "GroupUserModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 class='modal-title' id='exampleModalLongTitle'>"
                + action
                + " Member</h3>"
                + "      </div>"
                + "      <div class='modal-body'>"
                + "		<div class='form-group' id='"
                + actionLowerCase
                + "FormUserId'>"
                + "			<label style='margin-top:1em'>User id:</label>"
                + "			<input id='"
                + actionLowerCase
                + "GroupMemberNameInput' class='form-control'>"
                + " 		</div>"
                + "		</div>"
                + "      <div class='modal-footer' style='clear:both; background-color:#ececec; border-top: 2px solid #ccc;'>"
                + "	<span style='float:left;color:red;font-weight:bold;font-size:12pt' id='"
                + actionLowerCase
                + "GroupMemberMainErrSection'> </span>"
                + "        <button type='button' class='btn btn-secondary' data-dismiss='modal'>Close</button>"
                + "        <button type='button' class='btn btn-danger' id='btnConfirm" + action
                + "GroupMember' class='btn btn-primary'>" + action + " Member</button>"
                + "      </div>" + "    </div>" + "  </div>" + "</div>";

        return modalContruct;
    }

    // -------------RESOURCES--------------------------
    public String generateResourceModal(final String action) {
        final String name = action;
        final String idName = action.substring(0, 1).toLowerCase() + action.substring(1);
        final String modalContruct = "" + "<div class='modal fade bd-example-modal-lg' id='"
                + idName
                + "ResourceModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>"
                + "<div class='modal-dialog' role='document'>"
                + "<div class='modal-content'>"
                + "<div class='modal-header' Style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "<h3 style='color:white; margin: 0' id='exampleModalLongTitle'>"
                + name
                + " Resource</h3>"
                + " </div>"
                + "<div class='modal-body'>"
                + "<div id='topSection' style='width: 100%; height: 260px;'>"
                + "    <div id='forms' style=' float:left; width:57%; margin-right:2em; min-height:200px''> "
                + "	      <label>Resource Id:</label>"
                + "	      <input type='text' class='form-control' id='"
                + idName
                + "InputResourceId' disabled placeholder='autogenerated'>"
                + "	      <label>Resource Title:</label>"
                + "<div id='"
                + idName
                + "FormTitle'>"
                + "	      <input type='text' class='form-control' id='"
                + idName
                + "InputTitle'  placeholder='please add a description...'>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='"
                + idName
                + "InputTitleErrIcon'></span>"
                + "	     </div>"
                + "<div id='"
                + idName
                + "FormFormat'>"
                + " <label for='usr'>Format:</label>"
                + "	      <select class='form-control' id='"
                + idName
                + "SelType'>"
                + "				    <option value='Please select a format...'>Please select a format...</option>"
                + "				    <option value='Image'>Image</option>"
                + "				    <option value='Video'>Video</option>"
                + "				    <option value='Text'>Text</option>"
                + "				    <option value='PDF'>PDF</option>"
                + "				    <option value='Other'>Other</option>"
                + "			</select>"
                + "	    </div>"
                + "    </div>  "
                + "   <div id='preview' style='min-height:180px; float:left'>"
                + "   	<label>Resource Preview:</label>"
                + " <div class='responsive-container'>"
                + "  <div class='img-container'>"
                + "       <img  src='img/img_placeholder.png' style='max-width: 100%; max-height: 100%;' id='"
                + idName
                + "Output' alt='' />"
                + "   </div>"
                + "</div>"
                + "    </div>"
                + "</div> "
                + "<div id='"
                + idName
                + "FormUploadResource' style='clear:both; background-color:#ececec; border:2px solid #ccc;padding-left:1em;padding-top:1em;min-height: 7em;'>"
                + "	 <label for='usr'>File:</label><br>"
                + "	<label class='custom-file'>"
                + "		  <input type='file' id='"
                + idName
                + "ResourceInput' name='resourceFile' onchange='loadPreview(event)' class='custom-file-input'>"
                + "		<span style='float:left;color:red;font-weight:bold;font-size:10pt' id='"
                + idName
                + "UploadResourceErrSection'> </span>"
                + "	</label>"
                + "</div>"
                + "</div>"
                + "<div class='modal-footer' style='clear:both; background-color:#ececec; border-top: 2px solid #ccc;'>"
                + "	<span style='float:left;color:red;font-weight:bold;font-size:12pt' id='"
                + idName
                + "ResourceMainErrSection'> </span>"
                + "<button type='button' class='btn btn-secondary' data-dismiss='modal'>Close</button>"
                + "<button type='button' class='btn btn-danger' id='btnConfirm" + action
                + "Resource'>" + name + " Resource</button>" + "</div>" + "</div>" + "</div>"
                + "</div>";

        return modalContruct;
    }

    public String generateSigninModal() {

        final String modalSignin = ""
                + "<div class='modal fade' id='signinModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 id='exampleModalLongTitle'>Sign in hidden=true,</h3>"
                + "      </div>"
                + "      <div class='modal-body' align='center' style='min-height:10em'>"
                + "<form>"
                + " <table width='100%' style='border-collapse:separate; border-spacing:1em;'>"
                + " <tr>"
                + "	<td><label for='inputSigninUsername'>Username: </label></td>"
                + "	<td> "
                + " 		<div id='formGroupSigninUsername' class='form-group' role='form'>"
                + "			    <div class='col-sm-15'>"

                + "					<input type='text' class='form-control' id='inputSigninUsername' placeholder='Username'>"
                + "			      	<span class='glyphicon glyphicon-remove form-control-feedback' id='signinUsernameErrIcon'></span>"
                + "			     	<small id='signinUsernameErrMessage' class='form-text text-muted' style='color:red'></small>"
                
                + "			    </div>"
                + "		 </div>"
                + "	</td>"
                + "</tr>"
                + "<tr>"
                + "	<td><label for='inputSigninPassword'>Password: </label></td>"
                + "	<td> "
                + "		<div id='formGroupSigninPassword' class='form-group' role='form'>"
                + "			 <div class='col-sm-15'>"
                
                + "			      <input type='password' class='form-control' id='inputSigninPassword' placeholder='Password'>"
                + "			       <small id='signinPasswordErrMessage' class='form-text text-muted' style='color:red'></small>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signPasswordErrIcon'></span>"
                
                + "			    </div>"
                + "		 	 </div>"
                + "	</td>"
                + "</tr>"
                + "<tr>"
                + "	<td>Security Question (12+3)? </td>"
                + "	<td> "
                + "		<div id='formGroupSigninSecQuestion' class='form-group ' role='form'>"
                + "			    <div class='col-sm-15'>"

                + "			      <input type='text' class='form-control' id='inputSigninSecQuestion' placeholder='Security question'>"
                + "			       <small id='signinSecQuestionErrMessage' class='form-text text-muted' style='color:red'></small>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signinSecQuestionErrIcon'></span>"

                + "			    </div>"
                + "		 	 </div>"
                + "	</td>"
                + "</tr> "
                + "</table> "
                + "</form>"
                + "      </div>"
                + "      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
                + "		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='signinMainErrSection'> </span>"
                + "       <span style='float:right' ><button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
                + "       <button type='button' class='btn btn-danger' id='btnConfirmSignin'>Sign in</button>"
                + "      </span></div>" + "</div>" + "</div>" + "</div>";

        return modalSignin;
    }

    public String generateSignupModal() {
        final String modalSignup = ""
                + "<div class='modal fade' id='signupModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 id='exampleModalLongTitle'>Sign up hidden true,n ohne js</h3>"
                + "      </div>"
                + "      <div class='modal-body' align='center' style='min-height:10em'>"
                + " <table width='100%' style='border-collapse:separate; border-spacing:1em;'>"
                + " <tr>"
                + "	<td><label for='inputSignupUsername' Username: </label></td>"
                + "	<td> "
                + " 		<div id='formGroupSignupUsername' class='form-group' role='form'>"
                + "			    <div class='col-sm-15'>"
                +" 				<form>"			
                + "			      <input type='text' class='form-control' id='inputSignupUsername' placeholder='Username'>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupUsernameErrIcon'></span>"
                + "			     <small id='signupUsernameErrMessage' class='form-text text-muted' style='color:red'></small>"
                +"				</form>"	
                + "			    </div>"
                + "		 </div>"
                + "	</td>"
                + "</tr>"
                + "<tr>"
                + "	<td><label for='inputSignupEmail'>Email: </label></td>"
                + "	<td> "
                + "		<div id='formGroupSignupEmail' class='form-group' role='form'>"
                + "			 <div class='col-sm-15'>"
                +" 				<form>"		
                + "			      <input type='text' class='form-control' id='inputSignupEmail' placeholder='Email'>"
                + "			       <small id='signupEmailErrMessage' class='form-text text-muted' style='color:red'></small>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupEmailErrIcon'></span>"
                +"				</form>"	
                + "			    </div>"
                + "		 	 </div>"
                + "	</td>"
                + "</tr>"
                + "<tr>"
                + "	<td><label for='inputSignupPassword'>Password: </label></td>"
                + "	<td> "
                + "		<div id='formGroupSignupPassword' class='form-group' role='form'>"
                + "			 <div class='col-sm-15'>"
                +" 				<form>"	
                + "			      <input type='password' class='form-control' id='inputSignupPassword' placeholder='Password'>"
                + "			       <small id='signupPasswordErrMessage' class='form-text text-muted' style='color:red'></small>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupPasswordErrIcon'></span>"
                +"				</form>"	
                + "			    </div>"
                + "		 	 </div>"
                + "	</td>"
                + "</tr>"
                + "<tr>"
                + "	<td>Security Question (12+3)? </td>"
                + "	<td> "
                + "		<div id='formGroupSignupSecQuestion' class='form-group ' role='form'>"
                + "			    <div class='col-sm-15'>"
                +" 				<form>"	
                + "			      <input type='text' class='form-control' id='inputSignupSecQuestion' placeholder='Security question'>"
                + "			       <small id='signupSecQuestionErrMessage' class='form-text text-muted' style='color:red'></small>"
                + "			      <span class='glyphicon glyphicon-remove form-control-feedback' id='signupSecQuestionErrIcon'></span>"
                +"				</form>"	
                + "			    </div>"
                + "		 	 </div>"
                + "	</td>"
                + "</tr> "
                + "</table> "
                + "      </div>"
                + "      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
                + "		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='signupMainErrSection'> </span>"
                + "      <span style='float:right' > <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
                + "        <button type='button' class='btn btn-danger' id='btnConfirmSignup'>Sign up</button>"
                + "      </span></div>" + "</div>" + "</div>" + "</div>";

        return modalSignup;
    }

    public String generateViewContextModal(final String contextName) {
        String idName = "";
        if (contextName.contains("Context")) {
            idName = contextName.replace("-", "");
        } else {
            idName = contextName;
        }

        final String modalContruct = "" + "<div class='modal fade' id='view"
                + idName
                + "Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
                + "  <div class='modal-dialog' role='document'>"
                + "    <div class='modal-content'>"
                + "      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
                + "        <h3 id='exampleModalLongTitle'>View "
                + contextName
                + "</h3>"
                + "      </div>"
                + "      <div class='modal-body' style='min-height:10em; min-height: 10em; padding-bottom: 2em; padding-left: 2em; padding-top: 2em;'>"
                + "      <textarea disabled='true' style='width: 100% !important' id='inputView"
                + idName
                + "' class='form-control' rows='14'></textarea>"
                + " <span style='margin-right: 2em; margin-left:29em; margin-top:2em' id='view"
                + idName
                + "lbOutput'><b>Output:</b></span>"
                + "<select style='margin-top:1em' class='selectpicker' id='view"
                + idName
                + "OutputSelContentType'>"
                + "<option>JSON</option>"
                + "<option>XML</option>"
                + "</select>"
                + "      </div>"
                + "      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
                + "		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='edit"
                + idName
                + "MainErrSection'> </span>"
                + "       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
                + "       </div></div>" + "</div></div>";

        return modalContruct;
    }
}
