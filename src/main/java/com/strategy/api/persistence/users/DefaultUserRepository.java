package com.strategy.api.persistence.users;

import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.persistence.users.converters.UserJpaConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created on 2021-01-20.
 */
@Service
public class DefaultUserRepository implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserJpaConverter userJpaConverter;

    public DefaultUserRepository(final UserJpaRepository jpaRepository,
                                 final UserJpaConverter userJpaConverter) {
        this.jpaRepository = jpaRepository;
        this.userJpaConverter = userJpaConverter;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaConverter.toJpaEntity(user);
        userEntity = jpaRepository.save(userEntity);
        return userJpaConverter.fromJpaEntity(userEntity);
    }

    @Override
    public User edit(User user, Boolean changeUsername) {
        UserEntity existingUser = jpaRepository.findById(user.getId()).orElseThrow();

        existingUser.setName(user.getName());
        existingUser.setProfileImageUrl(user.getProfileImageUrl());
        existingUser.setTimeZone(user.getTimeZone());

        if(changeUsername) {
            existingUser.setUsername(user.getUsername());
        }

        jpaRepository.save(existingUser);
        return userJpaConverter.fromJpaEntity(existingUser);
    }


    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> userEntity = jpaRepository.findByUsername(email);

        return userEntity.map(userJpaConverter::fromJpaEntity).orElse(null);
    }

    @Override
    public User findByCedula(String cedula) {
        Optional<UserEntity> userEntity = jpaRepository.findByUsername(cedula);

        return userEntity.map(userJpaConverter::fromJpaEntity).orElse(null);
    }

    @Override
    public boolean userExist(String username) {
        return jpaRepository.existsUserEntityByUsername(username);
    }

    @Override
    public void increaseRegisteredCount(User user, int i) {
        jpaRepository.increaseRegisteredCount(user.getId(), i);
    }

    @Override
    public void increaseRegisteredCount(Long userId, int i) {
        jpaRepository.increaseRegisteredCount(userId, i);
    }

    @Override
    public void deleteByCedula(String cedula) {
        try {
            UserEntity userEntity = jpaRepository.findByUsername(cedula).orElseThrow();
            jpaRepository.delete(userEntity);
        } catch (Exception ignore){}

    }

    @Override
    public User findById(Long id) {
        Optional<UserEntity> userEntity = jpaRepository.findById(id);

        return userEntity.map(userJpaConverter::fromJpaEntity).orElse(null);
    }

    @Override
    public Integer getRegisteredCount(Long userId) {
        return jpaRepository.getRegisteredCount(userId);
    }

    @Override
    @Transactional
    public void updateCreatedBy(Long suId, Long coordinatorUserId) {
        jpaRepository.updateCreatedBy(suId, coordinatorUserId);
    }

}
