package com.strategy.api.base.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created on 9/26/18.
 */
public class BadRequestException extends RestResponseException {
    private final String message;
    public BadRequestException(final String message) {
        this.message = message;
    }

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

}
