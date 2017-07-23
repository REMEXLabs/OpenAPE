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
}
