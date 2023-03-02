package com.strategy.api.fileuploader.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 2020-09-27.
 */
public class FileIsNotImageException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
