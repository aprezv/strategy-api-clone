package com.strategy.api.domain.register.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/26/22.
 */
public class SimpatizanteRegisteredInSeException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.CONFLICT.value();
    }
}
