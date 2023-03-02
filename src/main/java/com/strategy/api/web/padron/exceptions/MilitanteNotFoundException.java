package com.strategy.api.web.padron.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/12/22.
 */
public class MilitanteNotFoundException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }
}
