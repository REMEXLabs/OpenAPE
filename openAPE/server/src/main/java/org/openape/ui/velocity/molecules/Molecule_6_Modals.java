package org.openape.ui.velocity.molecules;

public class Molecule_6_Modals {
	public String generateDeleteUserModal(){
		
	String modalContruct = "" 
		+ "<div class='modal fade' id='exampleModalLong' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
		+"  <div class='modal-dialog' role='document'>"
		+"    <div class='modal-content'>"
		+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
		+"        <h3 id='exampleModalLongTitle'>Delete user</h3>"
		+"      </div>"
		+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
		+"       <h4>Do you realy want to delete the user?</h4>"
		+"      </div>"
		+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
		+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
		+"        <button type='button' class='btn btn-danger' id='btnConfirmDeleteUser'>Delete User</button>"
		+"      </div>"
		+"</div>"
		+"</div>"
		+"</div>";
				
		return modalContruct;
	}
	
	
public String generateDeleteUserContextModal(){
		
		String modalContruct = "" 
			+ "<div class='modal fade' id='deleteUserContextModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
			+"  <div class='modal-dialog' role='document'>"
			+"    <div class='modal-content'>"
			+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
			+"        <h3 id='exampleModalLongTitle'>Delete user context</h3>"
			+"      </div>"
			+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
			+"       <h4>Do you realy want to delete the user context?</h4>"
			+"      </div>"
			+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
			+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
			+"        <button type='button' class='btn btn-danger' id='btnConfirmDeleteUserContext'>Delete user-context</button>"
			+"      </div>"
			+"</div>"
			+"</div>"
			+"</div>";
					
			return modalContruct;
		}

public String generateAddUserContextModal(){
	
	String modalContruct = "" 
		+ "<div class='modal fade' id='addUserContextModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
		+"  <div class='modal-dialog' role='document'>"
		+"    <div class='modal-content'>"
		+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
		+"        <h3 id='exampleModalLongTitle'>Add user-context</h3>"
		+"      </div>"
		+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
		+"      Add your Context in JSON-Format <textarea id='inputAdministrationAddUserContext' class='form-control' rows='10' id='comment'></textarea>"
		+"      </div>"
		+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
		+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='addUserContextMainErrSection'> </span>"	
		+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
		+"        <button type='button' class='btn btn-danger' id='btnConfirmAddUserContext'>Add user-context</button>"
		+"      </div>"
		+"</div>"
		+"</div>"
		+"</div>";
				
		return modalContruct;
	}

public String generateEditUserContextModal(){
	
	String modalContruct = "" 
		+ "<div class='modal fade' id='editUserContextModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
		+"  <div class='modal-dialog' role='document'>"
		+"    <div class='modal-content'>"
		+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
		+"        <h3 id='exampleModalLongTitle'>Edit user-context</h3>"
		+"      </div>"
		+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
		+"      Add your Context in JSON-Format <textarea id='inputAdministrationEditUserContext' class='form-control' rows='10' id='comment'></textarea>"
		+"      </div>"
		+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
		+"		<span style='float:left;color:red;font-weight:bold;padding-top:0.5em' id='editUserContextMainErrSection'> </span>"	
		+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
		+"        <button type='button' class='btn btn-danger' id='btnConfirmEditUserContext'>Edit user-context</button>"
		+"      </div>"
		+"</div>"
		+"</div>"
		+"</div>";
				
		return modalContruct;
	}
}
