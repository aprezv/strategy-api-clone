package com.strategy.api.auth.utils;

import com.strategy.api.persistence.users.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created on 2020-11-09.
 */
public class AuthUtils {
    public static UserEntity getCurrentUser(){
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
