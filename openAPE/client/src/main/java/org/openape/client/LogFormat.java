package org.openape.client;

import javax.ws.rs.core.Response;

public class LogFormat {

    public static String serverResponse(final Response response) {
        // TODO Auto-generated method stub
        final String message = "Server response code: " + response.getStatus() + "\n"
                + response.getStatusInfo();
        return message;
    }

}
