package org.openape.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoConfig {
    private static Logger logger = LoggerFactory.getLogger(MongoConfig.class);
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
    public static String getString(final String key) {
        try {
            final String value = MongoConfig.RESOURCE_BUNDLE.getString(key);
            MongoConfig.logger.debug("looking for key\"" + key + "\" in " + MongoConfig.BUNDLE_NAME
                    + ", found value: " + value);
            return value;
        } catch (final MissingResourceException | NullPointerException | ClassCastException e) {
            MongoConfig.logger.debug("no entry found for key: " + key);
            return null;
        }
    }

    private MongoConfig() {

    }
}
