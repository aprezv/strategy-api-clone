package com.strategy.api.auth.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 2021-01-13.
 */
public class UsernameNotFoundException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.NOT_FOUND.value();
    }
}
