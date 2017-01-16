package org.openape.server.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.NotFoundException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.openape.api.listing.Listing;
import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
    public ResourceRESTInterface(final ResourceRequestHandler requestHandler) {
        super();

        /**
         * Request 7.6.2 create resource.
         */
        Spark.post("/api/resource",
                "multipart/form-data",
                (req, res) -> {
                    String id = "";
                    try {
                        // try to receive the sent resource
                req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                        new MultipartConfigElement("/tmp", 100000000, 100000000, 1024));
                final String filename = req.raw().getPart("file").getSubmittedFileName();
                final Part uploadedFile = req.raw().getPart("file");
                try (final InputStream in = uploadedFile.getInputStream()) {
                    Files.copy(in, Paths.get("/tmp/" + filename));
                    in.close();
                }
                // handle the resource
                id = requestHandler.createResource(uploadedFile);
                uploadedFile.delete();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            } catch (final IllegalArgumentException e) {
                // occurs if the filename is taken or its not a file.
                res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
                return e.getMessage();
            }
            res.status(SuperRestInterface.HTTP_STATUS_CREATED);
            return id;
        });

        /**
         * Request 7.6.3 get resource by ID. Used to get a specific resource
         * identified by ID.
         */
        Spark.get("/api/resource/resource-id", (req, res) -> {
            // get the id;
                final String resourceId = req.params(":resource-id");

                // get the file from server.
                final File file = requestHandler.getResourceById(resourceId);

                // Add file contents as zip to response.
                res.raw().setContentType("application/octet-stream");
                res.raw().setHeader("Content-Disposition",
                        "attachment; filename=" + file.getName() + ".zip");
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
                        System.err.println("Resource file creation streams could not be closed.");
                    }

                } catch (final IOException e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                } catch (final IllegalArgumentException e) {
                    // file by this name is not found.
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                    return e.getMessage();
                }
                res.type("application/zip");
                res.status(SuperRestInterface.HTTP_STATUS_OK);
                return res.raw();
            });

        /**
         * Request 7.6.4 get resource from listing.
         */
        Spark.get("/api/resource?listing-id=listing-id&index=index", (req, res) -> {

            // get the parameters;
                final String listingId = req.params(":listing-id");
                final String listingIndex = req.params(":index");

                File file = null;
                try {
                    // get listing
                final Listing listing = requestHandler.getListingById(listingId);
                // get the files from server.
                final List<File> files = requestHandler.getResourceByListing(listing);
                // get the requested resource.
                final int index = Integer.parseInt(listingIndex);
                file = files.get(index);
            } catch (NotFoundException | IllegalArgumentException | IndexOutOfBoundsException e) {
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            }

            // Add file contents as zip to response.
            res.raw().setContentType("application/octet-stream");

            res.raw().setHeader("Content-Disposition",
                    "attachment; filename=" + file.getName() + ".zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(res
                    .raw().getOutputStream()));
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
                    System.err.println("Resource file creation streams could not be closed.");
                }

            } catch (final IOException e) {
                res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                return e.getMessage();
            } catch (final IllegalArgumentException e) {
                // file by this name is not found.
                res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
                return e.getMessage();
            }
            res.type("application/zip");
            res.status(SuperRestInterface.HTTP_STATUS_OK);
            return res.raw();
        });

        /**
         * Request 7.6.5 delete resource.
         */
        Spark.delete("/api/resource/resource-id", (req, res) -> {
            final String resourceId = req.params(":resource-id");
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
            return "";

        });

    }

}
