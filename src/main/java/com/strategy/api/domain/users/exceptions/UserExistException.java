package com.strategy.api.domain.users.exceptions;

import com.strategy.api.base.exceptions.RestResponseException;
import org.springframework.http.HttpStatus;

/**
 * Created on 6/14/22.
 */
public class UserExistException extends RestResponseException {
    @Override
    public int getStatus() {
        return HttpStatus.CONFLICT.value();
    }
}
