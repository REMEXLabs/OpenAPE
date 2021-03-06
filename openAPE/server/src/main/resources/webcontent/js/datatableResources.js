var url = window.location.origin;
$(document).ready(function() {
	//remove created th

	
	$('#editFormTitle').removeClass("has-error has-feedback");
	$('#editInputTitleErrIcon').hide();
	$('#addFormTitle').removeClass("has-error has-feedback");
	$('#addInputTitleErrIcon').hide();
	
	var resourceTable = $('#ResourceDataTable').DataTable( {
		"lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]]
    } ); 
	
	resourceTable.column( 2 ).visible( false );
	
	if(window.location.hash == "#resources"){
		openCity(event, "resources");
	}
	
	if(window.location.href.indexOf("myResources") != -1){
		$('#ResourceDataTable').find("th").each(function() { 
			if($(this).text() == "Owner"){
				var headerIndex = $(this).index();
				
				$('#ResourceDataTable').find("td").each(function() { 
					if($(this).index() == headerIndex){
						if($(this).text() != localStorage.getItem("username")){
							$('#ResourceDataTable').DataTable().row( $(this).parents('tr'))
					        .remove()
					        .draw();
						}
					}
				})
			}
		})
		resourceTable.column( 5 ).visible( false );
	}
	
	
    $('#btnConfirmDeleteResource').click(function(){ 
    	removeResource(window.resourceId);
    })
    
    $('#btnAddResource').click(function(){ 
    	$('#addResourceModal').modal('show');	
    	$('#addResourceModal').attr('aria-hidden', 'false');
    })
    
    $('#btnConfirmAddResource').click(function(){ 
    	checkFields("add");
    })
    
    $('#btnConfirmEditResource').click(function(){ 
    	checkFields("edit");
    })
    
    $('#inputTitleErrIcon').hide();
    
    $('#addResourceInput').change(function() {
    	$('#addUploadResourceErrSection').empty();
    })
    
    $('#editResourceInput').change(function() {
    	$('#editUploadResourceErrSection').empty();
    })
    
	$('#addResourceModal').on('hidden.bs.modal', function () {
		$('#addFormTitle').removeClass("has-error has-feedback");
		$('#addInputTitleErrIcon').hide();
		$('#addFormFormat').removeClass("has-error has-feedback");
		$('#addUploadResourceErrSection').empty();
		$('#addResourceMainErrSection').empty();
	});
    
	$('#editResourceModal').on('hidden.bs.modal', function () {
		$('#editFormTitle').removeClass("has-error has-feedback");
		$('#editInputTitleErrIcon').hide();
		$('#editFormFormat').removeClass("has-error has-feedback");
		$('#editUploadResourceErrSection').empty();
		$('#editResourceMainErrSection').empty();
	});
    
})

function deleteResource(event){
	$('#deleteResourceModal').modal('show');
	$('#deleteResourceModal').attr('aria-hidden', 'false');
	
	var id = event.id;
	var resourceId = $('#'+id).attr("data-resourceid");
	var resourceDescriptionId = $('#'+id).attr("data-resourcedescriptionid");
	
	window.resourceDescriptionId = resourceDescriptionId;
	window.resourceId = resourceId;
}

function editResource(event){
	$('#editFormUploadResource').css("display", "none");
	$('#editResourceModal').modal('show');
	$('#editResourceModal').attr('aria-hidden', 'false');
	if(getResourceDescription(event.id).status == 200){
		var response = getResourceDescription(event.id).responseJSON;
		var resourceDescription = JSON.stringify(response).replace("resource-description", "resourceDescription");
		var resourceDescriptionObj = JSON.parse(resourceDescription);
		
		var resourceId = resourceDescriptionObj.resourceDescription[0][1]
		.substring(resourceDescriptionObj.resourceDescription[0][1]
		.indexOf("resources")+10);
		
		var title = resourceDescriptionObj.resourceDescription[1][1];
		var format = resourceDescriptionObj.resourceDescription[2][1];
		
		$('#editInputResourceId').val(resourceId);
		$('#editInputTitle').val(title);
		$('#editSelType option[value="'+format+'"]').prop('selected', true);
		
	}
	window.resourceDescriptionId = event.id;
}



function checkFields(action){
	var isTitleCorrect = true;
	var isFormatCorrect = true;
	var isFileCorrect = true;
	
	var inpuTitle = $('#'+action+'InputTitle').val();
	var selectValue = $('#'+action+'SelType').val();
	var resource = $('#'+action+'ResourceInput').val();
	
	if(inpuTitle == ""){
		isTitleCorrect = false;
		$('#'+action+'FormTitle').addClass("has-error has-feedback");
		$('#'+action+'InputTitleErrIcon').show();
	} else {
		isTitleCorrect = true;
		$('#'+action+'FormTitle').removeClass("has-error has-feedback");
		$('#'+action+'InputTitleErrIcon').hide();
	}
	
	if( selectValue == "Please select a format...") {
		$('#'+action+'FormFormat').addClass("has-error has-feedback");
		isFormatCorrect = false;
	} else {
		isFormatCorrect = true;
		$('#'+action+'FormFormat').removeClass("has-error has-feedback");
	}
	
	if(action == "add"){
		if(resource == ""){
			$('#'+action+'UploadResourceErrSection').empty();
			$('#'+action+'UploadResourceErrSection').append("Please add a file!");
			isFileCorrect = false;
		} else {
			$('#'+action+'UploadResourceErrSection').empty();
			isFileCorrect = true;
		}
	} else {
		isFileCorrect = true;
	}
	
	
	if(isTitleCorrect && isFormatCorrect && isFileCorrect){
		if(action == "add") {
			uploadFile();
		} else {
			generateResourceDescriptionObject($("#editInputResourceId").val(), "edit");
		}
		
		$('#'+action+'ResourceMainErrSection').empty();
	} else {
		$('#'+action+'ResourceMainErrSection').empty();
		$('#'+action+'ResourceMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> Please check all fields");
	}
}

function generateResourceDescriptionObject(resourceId, action){
		var objResourceProperties = new Object();
		var objResourceProperty =  new Object();
		var arrResourceProperty = new Array();
		
		var arrResourceDescription = [];
		var arrResourceUri = [];
		arrResourceUri.push("resource-uri");
		arrResourceUri.push("https://res.openurc.org/api/resources/"+resourceId);
		
		var arrResourceTitle = [];
		arrResourceTitle.push("http://purl.org/dc/elements/1.1/title");
		arrResourceTitle.push($('#'+action+'InputTitle').val());
		
		var arrResourceFormat = [];
		arrResourceFormat.push("http://purl.org/dc/elements/1.1/format");
		arrResourceFormat.push($('#'+action+'SelType option:selected' ).text());
		arrResourceDescription.push(arrResourceUri);
		arrResourceDescription.push(arrResourceTitle);
		arrResourceDescription.push(arrResourceFormat);
		objResourceProperties.resourceDescription = arrResourceDescription;
		
		//objResourceProperty.name = "resource-uri";
		//objResourceProperty.value = "https://res.openurc.org/api/resources/"+resourceId;
		//arrResourceProperty.push(objResourceProperty);
		
		/*var objResourceProperty =  new Object();
		objResourceProperty.name = "http://purl.org/dc/elements/1.1/title";
		objResourceProperty.value = $('#'+action+'InputTitle').val();
		arrResourceProperty.push(objResourceProperty);
		
		var objResourceProperty =  new Object();
		objResourceProperty.name = "http://purl.org/dc/elements/1.1/format";
		objResourceProperty.value = $('#'+action+'SelType option:selected' ).text();
		arrResourceProperty.push(objResourceProperty)

		objResourceProperties.resourceDescription = arrResourceProperty;
		*/
		
		
		var formated = JSON.stringify(objResourceProperties).replace("resourceDescription", "resource-description");
		
		console.log(formated);
		if(action == "add"){	
			addResourceToMongoDB(formated);
		} else {
			var arrResourceModified = [];
			arrResourceModified.push("http://purl.org/dc/terms/modified");

			var currentdate = new Date();
			var month = currentdate.getUTCMonth() < 10 ? 0+""+(parseInt(currentdate.getUTCMonth())+1) : parseInt(currentdate.getUTCMonth())+1;
			var day = currentdate.getUTCDate() < 10 ? 0+""+currentdate.getUTCDate() : currentdate.getUTCDate();
			
			var updatedDate = currentdate.getFullYear()+"-"+month+"-"+currentdate.getUTCDate();

			arrResourceModified.push(updatedDate);
			
			arrResourceDescription.push(arrResourceModified);
			objResourceProperties.resourceDescription = arrResourceDescription;
			console.log(JSON.stringify(objResourceProperties));
			var formated = JSON.stringify(objResourceProperties).replace("resourceDescription", "resource-description");
			updateResourceDescription(window.resourceDescriptionId, formated);
		}
		
}

function removeResource(resourceId) {
	$.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: url+'/api/resources/'+resourceId,
        dataType: "json",
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	if(jqXHR.status == 204){
        		$('#deleteTaskContextModal').modal('hide');
            	setTimeout(function(){ 
            		location.reload();
           		}, 1000);
        	}
        },
        error: function(jqXHR, textStatus, errorThrown){
        	$('#deleteResourceMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> " +jqXHR.statusText);
        }
    });
}

function uploadFile(){
        var resData = new FormData();
        resData.append("resourceFile", document.getElementById("addResourceInput").files[0]);

        var req = new XMLHttpRequest();
              
        req.open("POST", url+'/api/resources', true);
        req.setRequestHeader("Authorization", localStorage.getItem("token"));
        
        req.send(resData);
        console.log("resource sent");
        console.log(req);
        req.onreadystatechange = function () {
            console.log(req.readyState, ", ", req.status);
            if (req.readyState === 4 && req.status === 201) {
                var resourceName = req.responseText;
                generateResourceDescriptionObject(resourceName, "add");
                $('#addResourceModal').modal('hide');
                undateAccessRights(resourceName);
                setTimeout(function(){ 
	        		location.reload();
	       		}, 1000);
            } else if (req.status !== 201)  {
        		$('#addResourceMainErrSection').empty();
        		$('#addResourceMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> "+req.response);
            }
        }
    }
function loadPreview (event){
		   var reader = new FileReader();
		    reader.onload = function(){
		      var output = "";
		      if(getTarget(event).attributes[1].nodeValue == "addResourceInput") {
		    	  output = document.getElementById('addOutput');
		      } else {
		    	  output = document.getElementById('editOutput');
		      }
		      output.src = reader.result;
		    };
		    reader.readAsDataURL(event.target.files[0]);	   
}


function getResourceDescription(resourceDescriptionId) {
	var status = "";
	$.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: url+'/api/resource-descriptions/'+resourceDescriptionId,
        dataType: "json",
        async: false,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	status =  jqXHR;
        },
        error: function(jqXHR, textStatus, errorThrown){
        	status = jqXHR;
        }
    });
	return status;
}


function addResourceToMongoDB(resource) {
	$.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: url+'/api/resource-descriptions',
        dataType: "json",
        data: resource,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	 console.log(jqXHR);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	 console.log(jqXHR);
        }
    });
}

function updateResourceDescription(resourceId, resourceDescription) {
	$.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: url+'/api/resource-descriptions/'+resourceId,
        dataType: "json",
        data: resourceDescription,
        headers: {
        	"Authorization": localStorage.getItem("token"),
        },
        success: function(data, textStatus, jqXHR){
        	 console.log("hier"+jqXHR);
        	

        },
        error: function(jqXHR, textStatus, errorThrown){
        	 if(jqXHR.status == 200){
        		 $('#editResourceModal').modal('hide');
                 setTimeout(function(){ 
 	        		location.reload();
 	       		}, 1000);
        	 }
        }
    });
}
function getTarget(obj) {
    var targ;
    var e=obj;
    if (e.target) targ = e.target;
    else if (e.srcElement) targ = e.srcElement;
    if (targ.nodeType == 3) // defeat Safari bug
        targ = targ.parentNode;
    return targ;
}