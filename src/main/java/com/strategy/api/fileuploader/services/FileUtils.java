package com.strategy.api.fileuploader.services;

import com.strategy.api.fileuploader.exceptions.FileIsNotImageException;
import com.strategy.api.fileuploader.models.FileUploadRequest;
import com.strategy.api.fileuploader.models.FileUploadResponse;
import com.strategy.api.fileuploader.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUtils {
    private FileServiceS3 fileServiceS3;

    public FileUtils(final FileServiceS3 fileServiceS3) {
        this.fileServiceS3 = fileServiceS3;
    }

    public String uploadMultipartFileAndReturnUrl(final MultipartFile multipartFile, String directory) throws IOException {
        if(!ImageUtils.isImage(multipartFile)){
            throw new FileIsNotImageException();
        }

        FileUploadResponse fileUploadResponse = uploadFile(multipartFile, directory);
        return fileUploadResponse.getUri();
    }

    public FileUploadResponse uploadFile(final MultipartFile multipartFile, final String directory) throws IOException{
        FileUploadRequest fileUploadRequest = FileUploadRequest
                .builder()
                .contentLength(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .inputStream(multipartFile.getInputStream())
                .fileName(multipartFile.getOriginalFilename())
                .key(String.format("%s/%s", directory, multipartFile.getOriginalFilename()))
                .publicAccess(true)
                .build();

        FileUploadResponse fileUploadResponse = this.fileServiceS3.uploadFile(fileUploadRequest);
        fileUploadResponse.setFileName(multipartFile.getOriginalFilename());
        fileUploadResponse.setSize(multipartFile.getSize());
        return fileUploadResponse;
    }

    public void deleteFile(final String url) {
        final String key = url.substring(url.lastIndexOf("/") + 1);
        fileServiceS3.deleteFile(key);
    }
}
