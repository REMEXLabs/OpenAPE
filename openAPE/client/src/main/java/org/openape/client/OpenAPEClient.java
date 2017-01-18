package org.openape.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.openape.api.usercontext.UserContext;



public class OpenAPEClient {
private Client client;
private WebTarget webResource;

public OpenAPEClient(String uri) {
	ClientConfig config = new ClientConfig();
	 client = ClientBuilder.newClient(config);
webResource = client.target(uri);
}
	
public URI createUserContext(UserContext userContext){
	Invocation.Builder invocationBuilder = webResource.request();

return null;
}


	public void getListing(String url) {
		Invocation.Builder invocationBuilder = webResource.request();


			 
				

	}
	
	
	public File getResource(URI uri, String targetFile){
		
        Invocation.Builder invocationBuilder = webResource.request();

Response response = invocationBuilder.get();

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			
			InputStream in = (InputStream) response.readEntity(InputStream.class);
//			
			File tf = new File(targetFile);
					try {
						Files.copy(in, tf.toPath());
		                 in.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}


		
		return tf;
	}

	public File getResource(String url, String targetFile) throws URISyntaxException {
		URI uri = new URI(url);
return 		getResource(uri, targetFile);
		
	}
	
}
