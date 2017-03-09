package org.openape.server.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.NotFoundException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.openape.api.Messages;
import org.openape.api.listing.Listing;
import org.openape.server.database.resources.ResourceList;
import org.openape.server.requestHandler.ResourceRequestHandler;

import spark.Spark;

public class ResourceRESTInterface extends SuperRestInterface {
    public static void setupResourceRESTInterface(final ResourceRequestHandler requestHandler) {
        /**
         * Request 7.6.2 create resource.
         */
        //        Spark.post(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"), //$NON-NLS-1$
        //                Messages.getString("ResourceRESTInterface.MultipartFormatDataMimeType"), //$NON-NLS-1$
        // (req, res) -> {
        //                    String id = Messages.getString("ResourceRESTInterface.EmptyString"); //$NON-NLS-1$
        // try {
        // // try to receive the sent resource
        // req.raw()
        // .setAttribute(
        //                                    Messages.getString("ResourceRESTInterface.jettyMultipartConfig"), //$NON-NLS-1$
        // new MultipartConfigElement(
        //                                            Messages.getString("ResourceRESTInterface.temporaryDiractory"), 100000000, 100000000, 1024)); //$NON-NLS-1$
        // final String filename = req
        // .raw()
        //                            .getPart(Messages.getString("ResourceRESTInterface.File")).getSubmittedFileName(); //$NON-NLS-1$
        // final Part uploadedFile = req.raw().getPart(
        //                            Messages.getString("ResourceRESTInterface.File")); //$NON-NLS-1$
        // try (final InputStream in = uploadedFile.getInputStream()) {
        // Files.copy(
        // in,
        // Paths.get(Messages
        //                                        .getString("ResourceRESTInterface.temporaryDiractoryWithFileseperator") + filename)); //$NON-NLS-1$
        // in.close();
        // }
        // // handle the resource
        // id = requestHandler.createResource(uploadedFile);
        // uploadedFile.delete();
        // } catch (final IllegalArgumentException e) {
        // // occurs if the filename is taken or its not a file.
        // res.status(SuperRestInterface.HTTP_STATUS_BAD_REQUEST);
        // return e.getMessage();
        // } catch (final Exception e) {
        // res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
        // e.printStackTrace();
        // return e.getMessage();
        // }
        // res.status(SuperRestInterface.HTTP_STATUS_CREATED);
        // return id;
        // });

        // Spark.post(Messages.getString("ResourceRESTInterface.ResourcesURLWithoutID"),
        // "multipart/form-data", (request, response) -> {
        // try{
        // String location = "image"; // the directory location where files will
        // be stored
        // long maxFileSize = 100000000; // the maximum size allowed for
        // uploaded files
        // long maxRequestSize = 100000000; // the maximum size allowed for
        // multipart/form-data requests
        // int fileSizeThreshold = 1024; // the size threshold after which files
        // will be written to disk
        //
        // MultipartConfigElement multipartConfigElement = new
        // MultipartConfigElement(
        // location, maxFileSize, maxRequestSize, fileSizeThreshold);
        // request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
        // multipartConfigElement);
        //
        // Collection<Part> parts = request.raw().getParts();
        // for (Part part : parts) {
        // System.out.println("Name: " + part.getName());
        // System.out.println("Size: " + part.getSize());
        // System.out.println("Filename: " + part.getSubmittedFileName());
        // }
        //
        // String fName = request.raw().getPart("file").getSubmittedFileName();
        // System.out.println("Title: " + request.raw().getParameter("title"));
        // System.out.println("File: " + fName);
        //
        // Part uploadedFile = request.raw().getPart("file");
        // Path out = Paths.get("image/" + fName);
        // try (final InputStream in = uploadedFile.getInputStream()) {
        // Files.copy(in, out);
        // uploadedFile.delete();
        // }
        // // cleanup
        // multipartConfigElement = null;
        // parts = null;
        // uploadedFile = null;
        //
        // return "OK";
        // } catch (Exception e) {
        // e.printStackTrace();
        // return e.getMessage();
        // }
        // });

        Spark.post(
                "/api/resource",
                (req, res) -> {
                    final File upload = new File("upload");
                    try {
                        if (!upload.exists() && !upload.mkdirs()) {
                            throw new RuntimeException("Failed to create directory "
                                    + upload.getAbsolutePath());
                        }

                        // apache commons-fileupload to handle file upload
                        DiskFileItemFactory factory = new DiskFileItemFactory();
                        factory.setRepository(upload);
                        ServletFileUpload fileUpload = new ServletFileUpload(factory);
                        List<FileItem> items = fileUpload.parseRequest(req.raw());

                        // image is the field name that we want to save
//                        FileItem item = items.stream()
//                                .filter(e -> "image".equals(e.getFieldName())).findFirst().get();
                        final File folder = new File(ResourceList.RESOURCEFOLDERPATH);
                        final File[] listOfFiles = folder.listFiles();
                        if (listOfFiles == null) {
                            // If directory does not exist, create
                            final boolean success = (new File(ResourceList.RESOURCEFOLDERPATH)).mkdirs();
                            if (!success) {
                                throw new IOException(
                                        Messages.getString("ResourceList.CouldNotCreateResourceFolderErrorMassage")); //$NON-NLS-1$
                            }
                            
                        }
                        FileItem item = items.get(0);
                        String fileName = FilenameUtils.getName(item.getName());
                        String fileNamePrefix = FilenameUtils.getBaseName(fileName) + "_";
                        String fileNameSuffix = "." + FilenameUtils.getExtension(fileName);
                        File file = File.createTempFile(fileNamePrefix, fileNameSuffix, folder);
                        item.write(file);
                        System.out.println("File successfully saved as " + file.getAbsolutePath());

                        final File fileToWrite = new File(ResourceList.RESOURCEFOLDERPATH + File.separator
                                + fileName);
                        item.write(fileToWrite);
                        return fileName;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }
                });
        /**
         * Request 7.6.3 get resource by ID. Used to get a specific resource
         * identified by ID.
         */
        Spark.get(Messages.getString("ResourceRESTInterface.ResourcesURLWithID"), (req, res) -> { //$NON-NLS-1$
                    // get the id;
                final String resourceId = req.params(Messages
                        .getString("ResourceRESTInterface.IDParam")); //$NON-NLS-1$

                // get the file from server.
                final File file = requestHandler.getResourceById(resourceId);

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

                } catch (final IOException e) {
                    res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                    return e.getMessage();
                } catch (final IllegalArgumentException e) {
                    // file by this name is not found.
                    res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
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

                    } catch (final IOException e) {
                        res.status(SuperRestInterface.HTTP_STATUS_INTERNAL_SERVER_ERROR);
                        return e.getMessage();
                    } catch (final IllegalArgumentException e) {
                        // file by this name is not found.
                        res.status(SuperRestInterface.HTTP_STATUS_NOT_FOUND);
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
