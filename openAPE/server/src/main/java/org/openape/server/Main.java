package org.openape.server;

import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.UserContext;
import org.openape.server.rest.SuperRestInterface;

/**
 * Starting class of the project. Creates REST APIs.
 */
public class Main {
    public static void main(String[] args) {
        // Start rest api.
        new SuperRestInterface();
    }

    /**
     * @return a sample user context representing someone with restricted
     *         vision.
     */
    public static UserContext sampleUserContextRestricedVision() {
        final UserContext restrictedVision = new UserContext();
        final Context restrictedViewPc = new Context("computer operation system", "0");
        final Context restrictedViewTicketMachine = new Context("ticket machine", "1");
        restrictedVision.addContext(restrictedViewPc);
        restrictedVision.addContext(restrictedViewTicketMachine);
        restrictedViewPc.addPreference("/smalltext", "screen magnifier");
        restrictedViewPc.addPreference("/longtext", "screen reader");
        restrictedViewTicketMachine.addPreference("/all", "high contrast");
        restrictedViewTicketMachine.addPreference("/text", "large font");
        return restrictedVision;
    }

}
