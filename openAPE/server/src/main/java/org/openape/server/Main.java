package org.openape.server;

import java.io.IOException;

import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;
import org.openape.server.database.mongoDB.DatabaseConnection;
import org.openape.server.database.resources.ResourceList;
import org.openape.server.rest.SuperRestInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    public static void main(String[] args) {
        // Start rest api and database connection.
        DatabaseConnection.getInstance();
        try {
            ResourceList.getInstance();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new SuperRestInterface();
    }

    /**
     * @return a sample user context representing someone with restriced vision.
     */
    public static UserContext sampleUserContextRestricedVision() {
        UserContext restrictedVision = new UserContext();
        Context restrictedViewPc = new Context("computer operation system", "0");
        Context restrictedViewTicketMachine = new Context("ticket machine", "1");
        restrictedVision.addContext(restrictedViewPc);
        restrictedVision.addContext(restrictedViewTicketMachine);
        restrictedViewPc.addPreference("/smalltext", "screen magnifier");
        restrictedViewPc.addPreference("/longtext", "screen reader");
        restrictedViewTicketMachine.addPreference("/all", "high contrast");
        restrictedViewTicketMachine.addPreference("/text", "large font");
        return restrictedVision;
    }

}
