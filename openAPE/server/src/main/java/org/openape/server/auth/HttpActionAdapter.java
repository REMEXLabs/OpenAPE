package org.openape.server.auth;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;

import spark.Spark;

public class HttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Object adapt(final int code, final SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            Spark.halt(401, "Unauthorized");
        } else if (code == HttpConstants.FORBIDDEN) {
            Spark.halt(403, "Forbidden");
        } else {
            return super.adapt(code, context);
        }
        return null;
    }

}
