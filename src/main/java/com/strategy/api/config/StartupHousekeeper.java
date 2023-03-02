package com.strategy.api.config;

import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created on 9/14/18.
 */
@Slf4j
@Component
public class StartupHousekeeper implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public StartupHousekeeper(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        List.of("support", "superadmin")
                .forEach(this::createUser);
    }

    @Transactional
    public void createUser(String username){

        User user = Optional.ofNullable(userRepository.findByEmail(username)).orElse(new User());

        if(user.getId() != null) {
            return;
        }

        user.setPassword(passwordEncoder.encode(username));
        user.setUsername(username);
        user.setName(username);
        user.setRole(User.Role.valueOf(username.toUpperCase()));
        userRepository.save(user);
    }

}
