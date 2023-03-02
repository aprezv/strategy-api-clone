package com.strategy.api.fileuploader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * Created on 30/4/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRequest {
    private InputStream inputStream;
    private String contentType;
    private long contentLength;
    private String key;
    private Boolean publicAccess = false;
    private Boolean resize = false;
    private Integer width;
    private String fileName;
}
