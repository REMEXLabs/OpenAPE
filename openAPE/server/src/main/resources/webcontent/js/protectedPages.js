/**This class provides functions to login to token protected 
 * 
 */
/*Sends an GET request to a page,
 * request header contains an authorisation token
 * 
 */
function requestProtectedPage(path) {
	//
	var token = localStorage.getItem("token")
	
	if (token !== null){
	$.ajax({
		type: "GET",
		beforeSend: function(request) {
			request.setRequestHeader("Authorization", token )},
			url: path,
//				success: processAjaxData(response), 
						}).done( function(response) {
						processAjaxData(response);	
						})						;
	}
}

/*Exchanges the html document in the dom
 * 
 */
function processAjaxData(response, urlPath){
	var newDoc = document.open("text/html", "replace");
	newDoc.write(response);
	newDoc.close();
}