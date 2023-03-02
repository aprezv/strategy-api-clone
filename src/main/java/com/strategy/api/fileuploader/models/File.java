package com.strategy.api.fileuploader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 30/4/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private String contentType;
    private Long contentLength;
    private byte[] content;
}
