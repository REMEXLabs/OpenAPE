package org.openape.ui.velocity.controllerComponents;

import java.io.IOException;

import org.openape.ui.velocity.organism.Organism_3_DataTable;
import org.openape.ui.velocity.requestHandler.MyContextsRequestHandler;

public class myContexts {
    public String test(final String userId) throws IllegalArgumentException, IOException {

        return new Organism_3_DataTable().generateMyContextUserContextTable(
                new MyContextsRequestHandler(), userId).toString();
    }

}
