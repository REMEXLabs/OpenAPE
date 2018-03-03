package org.openape.server.requestHandler;

import java.io.IOException;

import org.openape.api.ContextList;
public interface ContextRequestHandler {
    public ContextList<?> getAllContexts(String url) throws IOException;
    public ContextList<?> getMyContexts(String userId, String url) throws IOException;
    public ContextList<?> getPublicContexts(String url) throws IOException;
    
}
