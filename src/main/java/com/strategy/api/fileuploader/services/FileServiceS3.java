package com.strategy.api.fileuploader.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.strategy.api.fileuploader.models.File;
import com.strategy.api.fileuploader.models.FileUploadRequest;
import com.strategy.api.fileuploader.models.FileUploadResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log
@Service
public class FileServiceS3 implements FileService {

    private final AmazonS3Client amazonS3Client;
    private final String defaultBucket;

    public FileServiceS3(
            @Value("${cloud.aws.s3.default-bucket}") final String defaultBucket,
            final AmazonS3Client amazonS3Client) {

        this.amazonS3Client = amazonS3Client;
        this.defaultBucket = defaultBucket;
    }

    public FileUploadResponse uploadFile(final FileUploadRequest uploadRequest) {

        resizeIfNeeded(uploadRequest);

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadRequest.getContentType());
        metadata.setContentLength(uploadRequest.getContentLength());

        final String key = uploadRequest.getKey();

        final PutObjectRequest request = new PutObjectRequest(
                defaultBucket,
                key,
                uploadRequest.getInputStream(),
                metadata);

        if (uploadRequest.getPublicAccess()) {
            request.setCannedAcl(CannedAccessControlList.PublicRead);
        }

        amazonS3Client.putObject(request);
        FileUploadResponse response = new FileUploadResponse();
        response.setUri(request.getKey());
        response.setKey(request.getKey());

        if (uploadRequest.getPublicAccess()) {
            String uri = amazonS3Client.getResourceUrl(defaultBucket, key);
            response.setUri(uri);
        }

        return response;
    }

    public FileUploadResponse uploadFile(final FileUploadRequest uploadRequest, String bucket) {

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadRequest.getContentType());
        metadata.setContentLength(uploadRequest.getContentLength());

        final String key = uploadRequest.getKey();

        final PutObjectRequest request = new PutObjectRequest(
                bucket,
                key,
                uploadRequest.getInputStream(),
                metadata);

        if (uploadRequest.getPublicAccess()) {
            request.setCannedAcl(CannedAccessControlList.PublicRead);
        }

        amazonS3Client.putObject(request);
        FileUploadResponse response = new FileUploadResponse();
        response.setUri(request.getKey());
        response.setKey(request.getKey());

        if (uploadRequest.getPublicAccess()) {
            String uri = amazonS3Client.getResourceUrl(bucket, key);
            response.setUri(uri);
        }

        return response;
    }

    private void resizeIfNeeded(FileUploadRequest request){
        if(request.getResize() == null || !request.getResize()){
            return;
        }

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = request.getInputStream().read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();

            InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

            BufferedImage inputImage = ImageIO.read(is1);

            Integer originalH = inputImage.getHeight();
            Integer originalW = inputImage.getWidth();

            if(request.getWidth() > originalW){
                request.setInputStream(is2);
                return;
            }

            double ratio = (double) originalW / (double) originalH;

            int newW = request.getWidth();
            int newH = (int) Math.round(newW / ratio);

            // creates output image
            BufferedImage outputImage = new BufferedImage(newW,
                    newH, inputImage.getType());

            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, newW, newH, null);


            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ImageIO.write(outputImage, getExtension(request.getKey()), outStream);

            request.setInputStream(new ByteArrayInputStream(outStream.toByteArray()));
            request.setContentLength(outStream.size());

        } catch (Exception e){
            log.warning("Could not resize image. Exception is: " + e.getMessage());
        }

    }

    public File getFile(final String key) throws AmazonS3Exception, IOException {

        try {
            File file = new File();

            S3Object s3Object = amazonS3Client.getObject(defaultBucket, key);

            file.setContent(IOUtils.toByteArray(s3Object.getObjectContent()));
            file.setContentType(s3Object.getObjectMetadata().getContentType());
            file.setContentLength(s3Object.getObjectMetadata().getContentLength());
            return file;
        } catch (AmazonS3Exception e) {
            throw e;
        }
    }

    public void deleteFile(final String key) throws SdkClientException {
        amazonS3Client.deleteObject(defaultBucket, key);
    }

    private String getHashKey(final FileUploadRequest uploadRequest) {
        try {
            final byte[] data = uploadRequest.getInputStream().readAllBytes();
            uploadRequest.getInputStream().close();

            final String fileHash = getMD5FromBytes(data);

            InputStream bis = new ByteArrayInputStream(data);
            uploadRequest.setInputStream(bis);

            final int startOfFileName = uploadRequest.getKey().lastIndexOf(java.io.File.separator) + 1;
            final StringBuilder sbFinalKey = new StringBuilder();

            sbFinalKey
                    .append(uploadRequest.getKey(), 0, startOfFileName)
                    .append(fileHash)
                    .append("/")
                    .append(uploadRequest.getFileName());

            return sbFinalKey.toString();
        } catch (IOException e) {
        }

        return uploadRequest.getKey();
    }

    private String getExtension(final String fileName) {
        final int dotPosition = fileName.lastIndexOf(".");
        return fileName.substring(dotPosition + 1);
    }

    private String getMD5FromBytes(final byte[] uploadBytes) {
        MessageDigest md5;
        String hashString = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            hashString = new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }

        return hashString;
    }
}
