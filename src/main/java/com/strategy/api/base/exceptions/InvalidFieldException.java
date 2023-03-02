package com.strategy.api.base.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created on 2020-08-11.
 */
public class InvalidFieldException extends RestResponseException {
    private final String entity;
    private final String field;

    public InvalidFieldException(String entity, String field) {
        this.entity = entity;
        this.field = field;
    }

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public Object[] getParams() {
        return new Object[]{field, entity};
    }
}
