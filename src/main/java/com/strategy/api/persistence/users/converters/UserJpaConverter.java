package com.strategy.api.persistence.users.converters;

import com.strategy.api.domain.users.User;
import com.strategy.api.persistence.padron.MilitanteJpaConverter;
import com.strategy.api.persistence.users.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created on 2021-01-14.
 */
@Component
public class UserJpaConverter {
    private final MilitanteJpaConverter militanteJpaConverter;

    public UserJpaConverter(MilitanteJpaConverter militanteJpaConverter) {
        this.militanteJpaConverter = militanteJpaConverter;
    }

    public User fromJpaEntity(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map((u) ->  User.builder()
                        .name(u.getName())
                        .id(u.getId())
                        .username(u.getUsername())
                        .profileImageUrl(u.getProfileImageUrl())
                        .timeZone(u.getTimeZone())
                        .lastLogin(u.getLastLogin())
                        .createdDate(u.getCreatedDate())
                        .militante(militanteJpaConverter.fromJpaEntity(u.getMilitante()))
                        .role(u.getRole())
                        .registeredCount(u.getRegisteredCount())
                        .build())
                .orElse(null);
    }

    public UserEntity toJpaEntity(User user) {
        return Optional.ofNullable(user)
                .map((u) ->  UserEntity.builder()
                        .name(u.getName())
                        .id(u.getId())
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .profileImageUrl(u.getProfileImageUrl())
                        .timeZone(u.getTimeZone())
                        .role(u.getRole())
                        .registeredCount(u.getRegisteredCount())
                        .militante(militanteJpaConverter.toJpaEntity(u.getMilitante()))
                        .lastLogin(u.getLastLogin())
                        .build())
                .orElse(null);
    }
}
