
var urls = [];
urls[0] = "http://openape.gpii.eu/api";
urls[1] = "http://openape.gpii.eu/test/hello";

urls[2] = "http://dev.openape.gpii.eu/api";
urls[3] = "http://dev.openape.gpii.eu/test/hello";


  

urls.forEach(function(item, index, array) {

			let request = new XMLHttpRequest();
request.open("GET", item );

			request.onreadystatechange = function() {
				if ( request.readyState == 4 ){
console.log("" + request.status + " " + item );

} // if
} // onReadyChange
request.send(null);
});// forEach
