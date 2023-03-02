package com.strategy.api.web.register.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/23/22.
 */
public class CoordinatorNotExistsException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }
}
