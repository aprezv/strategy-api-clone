package com.strategy.api.base.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created on 9/26/18.
 */
public class EntityNotFoundException extends RestResponseException {
    private final String entityName;
    private final Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
    }

    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "No " + entityName + " found with ID " + entityId;
    }
}

