package com.strategy.api.domain.simpatizantes.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/13/22.
 */
public class SimpatizanteRegistradoException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.CONFLICT.value();
    }
}
