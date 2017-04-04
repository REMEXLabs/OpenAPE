package org.openape.server.auth;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import static spark.Spark.halt;

public class HttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Object adapt(int code, SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, "Unauthorized");
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, "Forbidden");
        } else {
            return super.adapt(code, context);
        }
        return null;
    }

}
