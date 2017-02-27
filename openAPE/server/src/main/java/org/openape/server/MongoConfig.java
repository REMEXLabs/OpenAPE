package org.openape.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MongoConfig {
    private static final String BUNDLE_NAME = "config/mongo"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(MongoConfig.BUNDLE_NAME);

    /**
     * Gets a string for the given key from this resource bundle or one of its
     * parents. Calling this method is equivalent to calling
     *
     * @param key
     *            the key for the desired string
     * @return the string for the given key or null if none found
     *
     */
    public static String getString(String key) {
        try {
            return MongoConfig.RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException | NullPointerException | ClassCastException e) {
            return null;
        }
    }

    private MongoConfig() {

    }
}
