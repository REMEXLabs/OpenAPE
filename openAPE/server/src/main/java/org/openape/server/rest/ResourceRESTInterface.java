package org.openape.server.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javassist.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.server.database.resources.GetResourceReturnType;
import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
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
    private static ResponseBuilder createFileResponse(final File file) throws IOException,
            FileNotFoundException {
        // Create file response
        final FileInputStream fileInputStream = new FileInputStream(file);
        final StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException {
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
            System.err.println(Messages
                    .getString("ResourceRESTInterface.StreamsCouldNotBeClosedErrorMassage")); //$NON-NLS-1$
        }
        final ResponseBuilder response = Response.ok(streamingOutput);
        return response;
    }

    public static void setupResourceRESTInterface(final ResourceRequestHandler requestHandler) {
        /**
         * Request 7.6.2 create resource.
         */
        Spark.post(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"), //$NON-NLS-1$
                (req, res) -> {

                    final String mimeType = req.contentType();
                    if (mimeType == null || mimeType.equals("")) {
                        res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                        return Messages.getString("ResourceRESTInterface.NoMimeTypeErrorMsg");//$NON-NLS-1$
            }
            // Return value.
            String fileName = Messages.getString("EmptyString"); //$NON-NLS-1$

            // Needed to process input file.
            final File tmpFile = new File(Messages.getString("ResourceRESTInterface.tmpFileName")); //$NON-NLS-1$
            try {
                if (!tmpFile.exists() && !tmpFile.mkdirs()) {
                    throw new RuntimeException(Messages
                            .getString("ResourceRESTInterface.FailedToCreateDirError") //$NON-NLS-1$
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
                fileName = requestHandler.createResource(item, mimeType);
            } catch (final IllegalArgumentException e) {
                // occurs if the filename is taken or its not a file.
                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
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
                final String resourceId = req.params(Messages
                        .getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$

                try {
                    // get the file from server.
                    final GetResourceReturnType serverReturn = requestHandler
                            .getResourceById(resourceId);
                    final File file = serverReturn.getFile();
                    final String mimeType = serverReturn.getMimeType();

                    // create response from file.
                    final ResponseBuilder response = ResourceRESTInterface.createFileResponse(file);
                    // Set meta information.
                    response.header("Content-Type", mimeType);
                    response.header("Content-Disposition", "inline; filename=" + file.getName());
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    // return file.
                    return response.build();
                } catch (final IllegalArgumentException e) {
                    // file by this name is not found.
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                    return e.getMessage();
                } catch (final Exception e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                }
            });

        /**
         * Request 7.6.4 get resource from listing.
         */
        Spark.get(
                Messages.getString("ResourceRESTInterface.ResourcesFromListingURL"), (req, res) -> { //$NON-NLS-1$

                    // get the parameters;
                    final String listingId = req.params(Messages
                            .getString("ResourceRESTInterface.ListingIDParam")); //$NON-NLS-1$
                    final String listingIndex = req.params(Messages
                            .getString("ResourceRESTInterface.IndexParam")); //$NON-NLS-1$

                    File file = null;
                    String mimeType = null;
                    try {
                        // get listing
                        final Listing listing = requestHandler.getListingById(listingId);
                        // get the files from server.
                        final List<GetResourceReturnType> serverResponse = requestHandler
                                .getResourceByListing(listing);
                        // get the requested resource.
                        final int index = Integer.parseInt(listingIndex);
                        final GetResourceReturnType returnType = serverResponse.get(index);
                        file = returnType.getFile();
                        mimeType = returnType.getMimeType();
                    } catch (NotFoundException | IllegalArgumentException
                            | IndexOutOfBoundsException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                    try {
                        // create response from file.
                        final ResponseBuilder response = ResourceRESTInterface
                                .createFileResponse(file);
                        // Set meta information.
                        response.header("Content-Type", mimeType);
                        response.header("Content-Disposition", "inline; filename=" + file.getName());
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
        Spark.delete(
                Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (req, res) -> { //$NON-NLS-1$
                    final String resourceId = req.params(Messages
                            .getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$
                    try {
                        requestHandler.deleteResourceById(resourceId);

                    } catch (final IllegalArgumentException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                    res.status(SuperRestInterface.HTTP_STATUS_NO_CONTENT);
                    return Messages.getString("ResourceRESTInterface.EmptyString"); //$NON-NLS-1$

                });

    }
}
