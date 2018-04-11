package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.ContextList;
public interface ContextRequestHandler {
    public  static 		ContextList getAllContexts(String url) throws IOException;
    /**
     * @param userId
     * @param url
     *            - the URL of the server on which the contexts are located,,
     *            usually the server on which this application is running on
     * @return
     * @throws IOException
     */
    public static ContextList<?> getMyContexts(String userId, String url) throws IOException;
    public ContextList<?> getPublicContexts(String url) throws IOException;
    
}
