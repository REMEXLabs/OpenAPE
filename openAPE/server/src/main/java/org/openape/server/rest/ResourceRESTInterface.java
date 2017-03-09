package org.openape.server.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.NotFoundException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
    public static void setupResourceRESTInterface(final ResourceRequestHandler requestHandler) {
        /**
         * Request 7.6.2 create resource.
         */
        Spark.post(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"), //$NON-NLS-1$
                (req, res) -> {
                    // Return value.
                String fileName = "";

                // Needed to process input file.
                final File tmpFile = new File("tmp");
                try {
                    if (!tmpFile.exists() && !tmpFile.mkdirs()) {
                        throw new RuntimeException("Failed to create directory "
                                + tmpFile.getAbsolutePath());
                    }
                    // apache commons-fileupload to handle file upload
                    final DiskFileItemFactory factory = new DiskFileItemFactory();
                    factory.setRepository(tmpFile);
                    final ServletFileUpload fileUpload = new ServletFileUpload(factory);
                    @SuppressWarnings("unchecked")//$NON-NLS-1$
                    // parseRequest() always reruns a List<FileItem>
                    final List<FileItem> items = fileUpload.parseRequest(req.raw());
                    final FileItem item = items.get(0);
                    // hand off file to handler.
                    fileName = requestHandler.createResource(item);
                } catch (final IllegalArgumentException e) {
                    // occurs if the filename is taken or its not a file.
                    res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                    return e.getMessage();
                } catch (final Exception e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                }
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
                    final File file = requestHandler.getResourceById(resourceId);

                    // Add file contents as zip to response.
                    res.raw().setContentType(
                            Messages.getString("ResourceRESTInterface.Octet-streamMimeType")); //$NON-NLS-1$
                    res.raw()
                            .setHeader(
                                    Messages.getString("ResourceRESTInterface.ContentDistribution"), //$NON-NLS-1$
                                    Messages.getString("ResourceRESTInterface.attatchment,Filename") + file.getName() + Messages.getString("ResourceRESTInterface.ZipFileEnding")); //$NON-NLS-1$ //$NON-NLS-2$
                    ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(
                            res.raw().getOutputStream()));
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            new FileInputStream(file));
                    final ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);
                    final byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bufferedInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.flush();

                    // try to close streams.
                    try {
                        zipOutputStream.close();
                        bufferedInputStream.close();
                    } catch (final IOException e) {
                        System.err.println(Messages
                                .getString("ResourceRESTInterface.StreamsCouldNotBeClosedErrorMassage")); //$NON-NLS-1$
                    }

                } catch (final IllegalArgumentException e) {
                    // file by this name is not found.
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                    return e.getMessage();
                } catch (final Exception e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                }
                res.type(Messages.getString("ResourceRESTInterface.ZipMimeType")); //$NON-NLS-1$
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                return res.raw();
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
                    try {
                        // get listing
                        final Listing listing = requestHandler.getListingById(listingId);
                        // get the files from server.
                        final List<File> files = requestHandler.getResourceByListing(listing);
                        // get the requested resource.
                        final int index = Integer.parseInt(listingIndex);
                        file = files.get(index);
                    } catch (NotFoundException | IllegalArgumentException
                            | IndexOutOfBoundsException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }

                    // Add file contents as zip to response.
                    res.raw().setContentType(
                            Messages.getString("ResourceRESTInterface.Octet-streamMimeType")); //$NON-NLS-1$

                    res.raw()
                            .setHeader(
                                    Messages.getString("ResourceRESTInterface.ContentDistribution"), //$NON-NLS-1$
                                    Messages.getString("ResourceRESTInterface.attatchment,Filename") + file.getName() + Messages.getString("ResourceRESTInterface.ZipFileEnding")); //$NON-NLS-1$ //$NON-NLS-2$
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                            new BufferedOutputStream(res.raw().getOutputStream()));
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                                    new FileInputStream(file))) {
                        final ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOutputStream.putNextEntry(zipEntry);
                        final byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bufferedInputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, len);
                        }
                        zipOutputStream.flush();

                        // try to close streams.
                        try {
                            zipOutputStream.close();
                            bufferedInputStream.close();
                        } catch (final IOException e) {
                            System.err.println(Messages
                                    .getString("ResourceRESTInterface.StreamsCouldNotBeClosedErrorMassage")); //$NON-NLS-1$
                        }

                    } catch (final IllegalArgumentException e) {
                        // file by this name is not found.
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                        return e.getMessage();
                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    }
                    res.type(Messages.getString("ResourceRESTInterface.ZipMimeType")); //$NON-NLS-1$
                    res.status(SuperRestInterface.HTTP_STATUS_OK);
                    return res.raw();
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
