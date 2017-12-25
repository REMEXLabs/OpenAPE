package org.openape.server.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.openape.api.Messages;
import org.openape.api.group.GroupAccessRights;
import org.openape.api.listing.Listing;
import org.openape.api.resourceDescription.ResourceObject;
import org.openape.api.user.User;
import org.openape.server.auth.AuthService;
import org.openape.server.auth.ResourceAuthService;
import org.openape.server.auth.UnauthorizedException;
import org.openape.server.database.resources.GetResourceReturnType;
import org.openape.server.requestHandler.ResourceRequestHandler;
import org.pac4j.core.profile.CommonProfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.NotFoundException;
import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
    private static final String GROUP_ACCESS_RIGHT_HEADER_NAME = "groupAccessRights";
    private static final String HEADER_MUST_CONTAIN_GROUP_ACCESS_RIGHT_MSG = "Header must contain a field named 'groupAccessRights' with a valid GroupAccessRights object as value.";
    private static final ResourceAuthService RESOURCE_AUTH_SERVICE = new ResourceAuthService();

    /**
     * Create a send able response from file.
     *
     * @param file
     *            on file system.
     * @return response
     * @throws IOException
     *             if an error occurs.
     * @throws FileNotFoundException
     *             if file is not found
     */
    private static ResponseBuilder createFileResponse(final File file) throws IOException, FileNotFoundException {
        // Create file response
        final FileInputStream fileInputStream = new FileInputStream(file);
        final StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(final OutputStream outputStream) throws IOException {
                try {
                    int n;
                    final byte[] buffer = new byte[1024];
                    while ((n = fileInputStream.read(buffer)) >= 0) {
                        outputStream.write(buffer, 0, n);
                    }
                    outputStream.close();
                } catch (final Exception e) {
                    throw new IOException(e);
                }

            }
        };
        try {
            fileInputStream.close();
        } catch (final IOException e) {
            System.err.println(Messages.getString("ResourceRESTInterface.StreamsCouldNotBeClosedErrorMassage")); //$NON-NLS-1$
        }
        final ResponseBuilder response = Response.ok(streamingOutput);
        return response;
    }

    public static void setupResourceRESTInterface(final ResourceRequestHandler requestHandler, final AuthService auth) {

        Spark.before(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"),
                auth.authorize("user"));

        Spark.before(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"),
                auth.authorize("user"));

        /**
         * Request 7.6.2 create resource.
         */
        Spark.post(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"), //$NON-NLS-1$
                (req, res) -> {

                    final String mimeType = req.headers(Messages.getString("ResourceRESTInterface.contentTypeString"));//$NON-NLS-1$
                    
                    // get group access rights.
                    GroupAccessRights groupAccessRights = null;
                    final String groupAccessRightsString = req.headers(ResourceRESTInterface.GROUP_ACCESS_RIGHT_HEADER_NAME);
                    // if else can be removed, if the client supports group access rights
                    if (groupAccessRightsString == null || !groupAccessRightsString.isEmpty()) {
                        //res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        //return ResourceRESTInterface.HEADER_MUST_CONTAIN_GROUP_ACCESS_RIGHT_MSG;
                        groupAccessRights = new GroupAccessRights();
                    }else{
                        try {
                            final ObjectMapper mapper = new ObjectMapper();
                            groupAccessRights = mapper.readValue(groupAccessRightsString, GroupAccessRights.class);
                        } catch (final Exception e) {
                            res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                            return ResourceRESTInterface.HEADER_MUST_CONTAIN_GROUP_ACCESS_RIGHT_MSG;
                        }
                    }

                    if ((mimeType == null) || mimeType.equals(Messages.getString("EmptyString"))) { //$NON-NLS-1$
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("ResourceRESTInterface.NoMimeTypeErrorMsg");//$NON-NLS-1$
                    }

                    // Return value.
                    String fileName = Messages.getString("EmptyString"); //$NON-NLS-1$

                    // Needed to process input file.
                    final File tmpFile = new File(Messages.getString("ResourceRESTInterface.tmpFileName")); //$NON-NLS-1$
                    try {
                        // get user from request response pair.
                        final User user = auth.getAuthenticatedUser(req, res);

                        if (!tmpFile.exists() && !tmpFile.mkdirs()) {
                            throw new RuntimeException(
                                    Messages.getString("ResourceRESTInterface.FailedToCreateDirError") //$NON-NLS-1$
                                            + tmpFile.getAbsolutePath());
                        }
                        // apache commons-fileupload to handle file upload
                        final DiskFileItemFactory factory = new DiskFileItemFactory();
                        factory.setRepository(tmpFile);
                        final ServletFileUpload fileUpload = new ServletFileUpload(factory);
                        @SuppressWarnings("unchecked")
                        // parseRequest() always returns a List<FileItem>
                        final List<FileItem> items = fileUpload.parseRequest(req.raw());
                        final FileItem item = items.get(0);
                        // hand off file to handler.
                        fileName = requestHandler.createResource(item, mimeType, user, groupAccessRights);
                    } catch (final IllegalArgumentException e) {
                        // occurs if the filename is taken or its not a file.
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return e.getMessage();
                    } catch (final UnauthorizedException e) {
                        // Only authorized users may post resources
                        res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                        return e.getMessage();
                    } catch (final Exception e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                    res.status(SuperRestInterface.HTTP_STATUS_CREATED);
                    return fileName;
                });

        /**
         * Request 7.6.3 get resource by ID. Used to get a specific resource
         * identified by ID.
         */
        Spark.get(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (req, res) -> { //$NON-NLS-1$
            // get the id;
            final String resourceId = req.params(Messages.getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$

            try {
                // get the file from server.
                final GetResourceReturnType serverReturn = requestHandler.getResourceById(resourceId);
                final File file = serverReturn.getFile();
                final String mimeType = serverReturn.getResourceObject().getMimeType();

                // check access right
                if ((serverReturn != null) && (serverReturn.getResourceObject() != null)) {
                    ResourceRESTInterface.RESOURCE_AUTH_SERVICE.allowReading(req, res,
                            serverReturn.getResourceObject());
                }

                // create response from file.
                final ResponseBuilder response = ResourceRESTInterface.createFileResponse(file);
                // Set meta information.
                response.header(Messages.getString("ResourceRESTInterface.ceontentTypeString"), mimeType); //$NON-NLS-1$
                response.header(Messages.getString("ResourceRESTInterface.contentDistributionString"), //$NON-NLS-1$
                        Messages.getString("ResourceRESTInterface.inlineFilename") + file.getName()); //$NON-NLS-1$
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                // return file.
                return response.build();
            } catch (final IllegalArgumentException e) {
                // file by this name is not found.
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final UnauthorizedException e) {
                // Only authorized users may read the resource.
                res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                return e.getMessage();
            } catch (final Exception e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

        /**
         * Request 7.6.4 get resource from listing.
         */
        Spark.get(Messages.getString("ResourceRESTInterface.ResourcesFromListingURL"), (req, res) -> { //$NON-NLS-1$

            // get the parameters;
            final String listingId = req.params(Messages.getString("ResourceRESTInterface.ListingIDParam")); //$NON-NLS-1$
            final String listingIndex = req.params(Messages.getString("ResourceRESTInterface.IndexParam")); //$NON-NLS-1$

            File file = null;
            String mimeType = null;
            try {
                // get listing
                final Listing listing = requestHandler.getListingById(listingId);
                // get the files from server.
                final List<GetResourceReturnType> serverResponse = requestHandler.getResourceByListing(listing);
                // get the requested resource.
                final int index = Integer.parseInt(listingIndex);
                final GetResourceReturnType returnType = serverResponse.get(index);
                file = returnType.getFile();
                mimeType = returnType.getResourceObject().getMimeType();

                // check access right
                if ((returnType != null) && (returnType.getResourceObject() != null)) {
                    ResourceRESTInterface.RESOURCE_AUTH_SERVICE.allowReading(req, res, returnType.getResourceObject());
                }

            } catch (NotFoundException | IllegalArgumentException | IndexOutOfBoundsException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            } catch (final UnauthorizedException e) {
                // Only authorized users may read the resource.
                res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                return e.getMessage();
            }
            try {
                // create response from file.
                final ResponseBuilder response = ResourceRESTInterface.createFileResponse(file);
                // Set meta information.
                response.header(Messages.getString("ResourceRESTInterface.ceontentTypeString"), mimeType); //$NON-NLS-1$
                response.header(Messages.getString("ResourceRESTInterface.contentDistributionString"), //$NON-NLS-1$
                        Messages.getString("ResourceRESTInterface.inlineFilename") + file.getName()); //$NON-NLS-1$
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                // return file.
                return response.build();

            } catch (final IllegalArgumentException e) {
                // file by this name is not found.
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
        });

        /**
         * Request 7.6.5 delete resource.
         */
        Spark.delete(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (req, res) -> { //$NON-NLS-1$
            final String resourceId = req.params(Messages.getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$
            try {
                // get user from request response pair.
                final CommonProfile profile = auth.getAuthenticatedProfile(req, res);

                // get resource object
                final GetResourceReturnType serverReturn = requestHandler.getResourceById(resourceId);

                // check access right
                if ((serverReturn != null) && (serverReturn.getResourceObject() != null)) {
                    ResourceRESTInterface.RESOURCE_AUTH_SERVICE.allowDeleting(req, res,
                            serverReturn.getResourceObject());
                }

                requestHandler.deleteResourceById(resourceId, profile);
            } catch (final IllegalArgumentException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            } catch (final UnauthorizedException e) {
                // Only authorized users may delete their resources.
                res.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                return e.getMessage();
            }
            res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
            return Messages.getString("ResourceRESTInterface.EmptyString"); //$NON-NLS-1$

        });
        
        // TODO not tested!
        Spark.head(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (request, response) -> { //$NON-NLS-1$
            System.out.println("head was called");
            final String resourceId = request.params(Messages.getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$
            System.out.println("resourceId "+resourceId);
            
            try{
                final GetResourceReturnType serverReturn = requestHandler.getResourceById(resourceId);
                final ResourceObject resourceObject = serverReturn.getResourceObject();
                final ObjectMapper mapper = new ObjectMapper();
                System.out.println(mapper.writeValueAsString(resourceObject));
                GroupAccessRights groupAccessRights = resourceObject.getGroupAccessRights();
                if(groupAccessRights == null || groupAccessRights.getGroupAccessRights() == null){
                    groupAccessRights = new GroupAccessRights();
                }
                response.header(ResourceRESTInterface.GROUP_ACCESS_RIGHT_HEADER_NAME, mapper.writeValueAsString(groupAccessRights));
                response.status(SuperRestInterface.HTTP_STATUS_OK);
            } catch (final IllegalArgumentException e) {
                response.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final Exception e) {
                response.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
            
            return Messages.getString("ResourceRESTInterface.EmptyString"); //$NON-NLS-1$
        });
        
        // TODO not tested!
        Spark.patch(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (request, response) -> { //$NON-NLS-1$
            System.out.println("patch was called");
            final String resourceId = request.params(Messages.getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$
            
            // get group access rights.
            final String groupAccessRightsString = request.headers(ResourceRESTInterface.GROUP_ACCESS_RIGHT_HEADER_NAME);
            
            try {
                // get user from request response pair.
                final CommonProfile profile = auth.getAuthenticatedProfile(request, response);
                
                if (groupAccessRightsString == null || groupAccessRightsString.isEmpty()) {
                    throw new IllegalArgumentException(ResourceRESTInterface.HEADER_MUST_CONTAIN_GROUP_ACCESS_RIGHT_MSG);
                }
                final ObjectMapper mapper = new ObjectMapper();
                final GroupAccessRights groupAccessRights = mapper.readValue(groupAccessRightsString, GroupAccessRights.class);
                final GetResourceReturnType serverReturn = requestHandler.getResourceById(resourceId);
                final ResourceObject resourceObject = serverReturn.getResourceObject();
                resourceObject.setGroupAccessRights(groupAccessRights);
                requestHandler.updateResourceById(resourceObject, profile);    
            }  catch (final UnauthorizedException e) {
                // Only authorized users may update their resources.
                response.status(SuperRestInterface.HTTP_STATUS_UNAUTHORIZED);
                return e.getMessage();    
            } catch (final IllegalArgumentException e) {
                response.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                return e.getMessage();
            }  catch (final IOException e) {
                response.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }
            
            response.status(SuperRestInterface.HTTP_STATUS_OK);
            return Messages.getString("ResourceRESTInterface.EmptyString"); //$NON-NLS-1$
        });

    }
}
