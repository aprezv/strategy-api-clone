package com.strategy.api.domain.padron.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/14/22.
 */
public class DocumentNumberNotFoundException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }
}
