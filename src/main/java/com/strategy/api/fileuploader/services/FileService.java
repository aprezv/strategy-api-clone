package com.strategy.api.fileuploader.services;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.strategy.api.fileuploader.models.File;
import com.strategy.api.fileuploader.models.FileUploadRequest;
import com.strategy.api.fileuploader.models.FileUploadResponse;

import java.io.IOException;


/**
 * Created on 30/4/2017.
 */
public interface FileService {
    FileUploadResponse uploadFile(final FileUploadRequest fileUploadRequest);
    File getFile(final String key) throws AmazonS3Exception, IOException;
}
