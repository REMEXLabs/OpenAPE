package org.openape.ui.velocity.molecules;

public class Molecule_6_Modals {

public String generateDeleteContextModal(String contextName){
	
	String idName = "";
	if(contextName.contains("Context")){
		idName = contextName.replace("-", "");
	} else {
		idName = contextName;
	}

	String modalContruct = "" 
		+ "<div class='modal fade' id='delete"+idName+"Modal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLongTitle' aria-hidden='true'>"
		+"  <div class='modal-dialog' role='document'>"
		+"    <div class='modal-content'>"
		+"      <div class='modal-header' style='background-color:#e31134; color:white;text-align:center;border-bottom: 2px solid #AB112A;'>"
		+"        <h3 id='exampleModalLongTitle'>Delete "+contextName+"</h3>"
		+"      </div>"
		+"      <div class='modal-body' align='center' style='min-height:10em;padding:5em'>"
		+"       <h4>Do you realy want to delete the "+contextName+" ?</h4>"
		+"      </div>"
		+"      <div class='modal-footer' style='background-color:#ececec;border-top: 2px solid #ccc;'>"
		+"       <button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancel</button>"
		+"        <button type='button' class='btn btn-danger' id='btnConfirmDelete"+idName+"'>Delete "+contextName+"</button>"
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
}
