package org.openape.server.rest;

import java.io.File;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import spark.Spark;

public class ResourceManagerRESTInterface extends SuperRestInterface {
	public static void setupResourceManagerRESTInterface() {
		/**
		 * Get HTML interface.
		 */
		Spark.get("/resource-manager", (req, res) -> {
			res.type("text/html");
			// Spark.staticFileLocation("/webcontent");
			// String content = "";
			// try {
			// content = Files.toString(new File("WEB-INF" + File.separator +
			// "classes" + File.separator + "webcontent"
			// + File.separator + "resource-manager.html"), Charsets.UTF_8);
			// } catch (Exception e) {
			// e.printStackTrace();
			// System.out.println(e.getClass());
			// }
			res.status(SuperRestInterface.HTTP_STATUS_OK);
			String resourceManager = "<!DOCTYPE html>" + "\n<html lang='de'>" + "\n<head>" + "\n<meta charset='UTF-8'>"
					+ "\n<title>openAPE</title>" + "\n<script>"
					+ "\nvar serveraddress = 'http://localhost:8080/server-0.1-SNAPSHOT';" + "\nfunction uploadFile() {"
					+ "\nvar resData = new FormData();"
					+ "\nresData.append('resourceFile', document.getElementById('resourceInput').files[0]);"
					+ "\nvar req = new XMLHttpRequest();"
					+ "\nreq.open('POST', serveraddress + '/api/resource', true);" + "\nreq.send(resData);"
					+ "\nconsole.log('resource sent');" + "\nreq.onreadystatechange = function () {"
					+ "\nconsole.log('Point1');" + "\nconsole.log('Status: ',req.status);"
	                + "\nconsole.log('readyState: ',req.readyState);" + "\nif (req.readyState === 4 && req.status === 201) {"
					+ "\nconsole.log('Point2');" + "\nvar resourceName = req.getResponseHeader('Location');"
					+ "\nconsole.log('resource name: ', resourceName);" + "\nuploadDescription(resourceName);" + "\n}"
					+ "\n}" + "\n}" + "\nfunction uploadDescription(resourceName) {" + "\nvar json = [];"
					+ "\nvar resName = {'name': resourceName};" + "\njson.push(resName);"
					+ "\n$('tbody tr').each(function () {" + "\nvar data = {}," + "\n$td = $(this).find('td input'),"
					+ "\nkey = $td.eq(0).val()," + "\nvalue = $td.eq(1).val();" + "\ndata[key] = value;"
					+ "\njson.push(data);" + "\n});" + "\nalert(JSON.stringify(json));"
					+ "\nvar req = new XMLHttpRequest();"
					+ "\nreq.open('POST', serveraddress + '/api/resource-description', true);"
					+ "\nreq.send(JSON.stringify(json));" + "\nconsole.log('description sent: ', JSON.stringify(json));"
					+ "\n}" + "\n</script>" + "\n</head>" + "\n<body>" + "\n<table>" + "\n<thead>" + "\n<tr>"
					+ "\n<td>Name</td>" + "\n<td>Value</td>" + "\n</tr>" + "\n</thead>" + "\n<tbody>" + "\n<tr>"
					+ "\n<td><input type='text'></td>" + "\n<td><input type='text'></td>" + "\n</tr>" + "\n<tr>"
					+ "\n<td><input type='text'></td>" + "\n<td><input type='text'></td>" + "\n</tr>" + "\n<tr>"
					+ "\n<td><input type='text'></td>" + "\n<td><input type='text'></td>" + "\n</tr>" + "\n<tr>"
					+ "\n<td><input type='text'></td>" + "\n<td><input type='text'></td>" + "\n</tr>" + "\n<tr>"
					+ "\n<td><input type='text'></td>" + "\n<td><input type='text'></td>" + "\n</tr>" + "\n</tbody>"
					+ "\n</table>"
					+ "\n<button type='button' id='createJson'  onclick='uploadFile()'>Speichern</button><br>"
					+ "\n<input type='file' id='resourceInput' name='resourceFile'>"
					+ "\n<!--<button onclick='showName()'>ShowName</button>-->" + "\n</body>" + "\n</html>";
			return resourceManager;
		});
	}

}
