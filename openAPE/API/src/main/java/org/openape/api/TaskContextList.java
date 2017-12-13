package org.openape.api;

import java.util.List;

import org.openape.api.taskcontext.TaskContext;

public class TaskContextList extends ContextList<TaskContext> {

    public TaskContextList(List<TaskContext> contexts, String url) {
        super(contexts, url, "task-context-type");
    }

    }
