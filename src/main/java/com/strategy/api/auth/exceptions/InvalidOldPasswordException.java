package com.strategy.api.auth.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 2020-08-03.
 */
public class InvalidOldPasswordException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.NOT_ACCEPTABLE.value();
    }
}
