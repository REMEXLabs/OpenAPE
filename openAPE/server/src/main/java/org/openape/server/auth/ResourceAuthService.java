package org.openape.server.auth;

import org.openape.api.Resource;

import spark.Request;
import spark.Response;

/**
 * This class is the authentication service for resources. It checks if the
 * logged in user is allowed to do an action with an resource or not. Therefore
 * it checks, whether the logged in user is an OpenAPE admin, the owner of the
 * resource or a member of a group, which has the needed right.
 *
 * @author Tobias Ableitner
 *
 */
public class ResourceAuthService extends AuthService {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
   
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // getters and setters
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // abstract methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // override methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // public methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    public void allowDeleting(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        // TODO implement
    }

    public void allowReading(final Request request, final Response response, final Resource resource)
            throws UnauthorizedException {
        // TODO implement
    }

    public void allowRightsChanging(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        // TODO implement
    }

    public void allowUpdating(final Request request, final Response response,
            final Resource resource) throws UnauthorizedException {
        // TODO implement
    }

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // protected methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // private methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************


    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
}
