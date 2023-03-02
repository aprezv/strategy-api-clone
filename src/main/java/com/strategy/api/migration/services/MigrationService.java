package com.strategy.api.migration.services;

import com.strategy.api.domain.users.PromoteService;
import com.strategy.api.persistence.users.UserEntity;
import com.strategy.api.persistence.users.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created on 6/20/22.
 */
@Slf4j
@Component
public class MigrationService implements CommandLineRunner {
    private final UserJpaRepository userRepository;
    private final PromoteService promoteService;
    private final PasswordEncoder passwordEncoder;

    public MigrationService(final UserJpaRepository userRepository,
                            final PromoteService promoteService,
                            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.promoteService = promoteService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) {
        setPasswords();
    }


    @Transactional
    public void setPasswords() {
        List<UserEntity> users = userRepository.findAllByPasswordIsNull();

        users.forEach(u -> {
            u.setPassword(passwordEncoder.encode(promoteService.getPassword(u.getMilitante())));
            userRepository.save(u);
        });

        log.info("Setting password completed");
    }

}
