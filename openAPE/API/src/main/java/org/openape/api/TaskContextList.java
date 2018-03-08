package org.openape.api;

import java.net.URI;
import java.util.List;

import org.openape.api.taskcontext.TaskContext;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TaskContextList extends ContextList<TaskContext> {

    public TaskContextList(List<TaskContext> contexts, String url) {
        super(contexts, url, "task-context-uri");
    }
    
    public TaskContextList() {
        super();
    }
    
    public List<URI> getTaskContextUris() {
        return getContextUris();
    }

    @JsonSetter("task-context-uris")
    public void setTaskContextUris(final List<URI> taskContextUris) {
        setContextUris(taskContextUris);
    }


}
