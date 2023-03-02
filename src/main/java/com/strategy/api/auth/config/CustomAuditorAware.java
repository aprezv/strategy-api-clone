package com.strategy.api.auth.config;

import com.strategy.api.persistence.users.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Created on 2020-08-07.
 */
public class CustomAuditorAware implements AuditorAware<UserEntity> {
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.ofNullable(null);
        }

        try {

            return Optional.ofNullable((UserEntity) authentication.getPrincipal());
        } catch (Exception e){
            return Optional.ofNullable(null);
        }
    }
}
