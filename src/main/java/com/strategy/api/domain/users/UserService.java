package com.strategy.api.domain.users;

import com.strategy.api.auth.events.UserCreatedEvent;
import com.strategy.api.auth.exceptions.InvalidOldPasswordException;
import com.strategy.api.auth.utils.AuthUtils;
import com.strategy.api.base.exceptions.BadRequestException;
import com.strategy.api.base.model.http.EditFieldRequest;
import com.strategy.api.base.services.BaseService;
import com.strategy.api.base.utils.FieldUtils;
import com.strategy.api.base.utils.MethodUtils;
import com.strategy.api.config.AppProperties;
import com.strategy.api.domain.padron.MilitanteRepository;
import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.users.events.PasswordResetSuccessEvent;
import com.strategy.api.persistence.users.UserEntity;
import com.strategy.api.persistence.users.UserJpaRepository;
import com.strategy.api.persistence.users.converters.UserJpaConverter;
import com.strategy.api.web.padron.exceptions.MilitanteNotFoundException;
import com.strategy.api.web.users.dto.CreateUserRequest;
import com.strategy.api.web.users.dto.RecoverChangePasswordRequest;
import com.strategy.api.web.users.dto.SendRecoverLinkRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

/**
 *
 */
@Service
public class UserService extends BaseService<UserEntity, Long> implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final AppProperties appProperties;
    private final MessageSource messageSource;
    private final UserJpaConverter userJpaConverter;
    private final FieldUtils fieldUtils;
    private final UserRepository userRepository;
    private final MilitanteRepository militanteRepository;
    private final PromoteService promoteService;

    public UserService(final UserJpaRepository userJpaRepository,
                       final PasswordEncoder passwordEncoder,
                       final @Qualifier("emailMessageSource") MessageSource messageSource,
                       final AppProperties appProperties,
                       final ApplicationEventPublisher eventPublisher,
                       final UserJpaConverter userJpaConverter,
                       final UserRepository userRepository,
                       final MilitanteRepository militanteRepository,
                       final PromoteService promoteService) {
        super(UserEntity.class);
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.appProperties = appProperties;
        this.messageSource = messageSource;
        this.userJpaConverter = userJpaConverter;
        this.userRepository = userRepository;
        this.militanteRepository = militanteRepository;
        this.promoteService = promoteService;
        this.fieldUtils = new FieldUtils(UserEntity.class, "User");
    }

    @Override
    public UserJpaRepository getRepository() {
        return userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userJpaRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public User editField(UserEntity user, EditFieldRequest request) {

        if (request.getFieldName().equals("email")) {
            //request.setFieldName("username");
            throw new BadRequestException("user is not allowed to change email address");
        }

        fieldUtils.updateField(user, request);
        userJpaRepository.save(user);

        return userJpaConverter.fromJpaEntity(user);
    }

    public void resetPassword(String cedula) {
        UserEntity userEntity = userJpaRepository.findByUsername(cedula)
                .orElseThrow(com.strategy.api.auth.exceptions.UsernameNotFoundException::new);
        Militante militante = Optional.ofNullable(militanteRepository.findByCedula(cedula))
                .orElseThrow(MilitanteNotFoundException::new);
        userEntity.setPassword(passwordEncoder.encode(promoteService.getPassword(militante)));
        userJpaRepository.save(userEntity);
    }

    @Transactional
    public UserEntity createUser(CreateUserRequest request) {

        UserEntity user = UserEntity.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getEmail().toLowerCase())
                .name(request.getName())
                .verified(request.getVerified())
                .active(request.getActive())
                .recoveryToken(request.getActivationToken())
                .profileImageUrl(request.getProfileImageUrl())
                .build();

        try {
            TimeZone.getTimeZone(request.getTimeZone());
            user.setTimeZone(request.getTimeZone());
        } catch (Exception e) {
            user.setTimeZone(ZoneId.systemDefault().getId());
        }

        userJpaRepository.save(user);

        eventPublisher.publishEvent(
                UserCreatedEvent.builder()
                        .source(this)
                        //TODO: remove user entity
                        .user(userJpaConverter.fromJpaEntity(user))
                        .build()
        );

        return user;
    }

    public Boolean userExists(String username) {
        return userJpaRepository.findByUsername(username).isPresent();
    }

    public void changePassword(String oldPassword, String newPassword) throws InvalidOldPasswordException {

        UserEntity currentUser = AuthUtils.getCurrentUser();

        if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userJpaRepository.save(currentUser);
            return;
        }

        throw new InvalidOldPasswordException();

    }

    public void sendRecoveryLink(SendRecoverLinkRequest request) {
        UserEntity user = userJpaRepository.findByUsername(request.getEmail())
                .orElseThrow(com.strategy.api.auth.exceptions.UsernameNotFoundException::new);

        String recoveryToken = UUID.randomUUID().toString();
        user.setRecoveryToken(recoveryToken);
        userJpaRepository.save(user);

        String link = String.format("%s/auth/reset-password?token=%s", appProperties.getBaseDomain(), recoveryToken);

        String subject = messageSource.getMessage(
                MethodUtils.getMessageCode("subject"),
                null,
                LocaleContextHolder.getLocale());

    }

    public void changePassword(RecoverChangePasswordRequest request) {
        UserEntity user = findUserByRecoveryPasswordToken(request);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setRecoveryToken(null);
        userJpaRepository.save(user);
        eventPublisher.publishEvent(
                PasswordResetSuccessEvent
                        .builder()
                        .user(userJpaConverter.fromJpaEntity(user))
                        .source(this)
                        .build());
    }

    public UserEntity findUserByRecoveryPasswordToken(RecoverChangePasswordRequest request) {
        return findUserByRecoveryPasswordToken(request.getToken());
    }

    public UserEntity findUserByRecoveryPasswordToken(String token) {
        return userJpaRepository.findByRecoveryToken(token)
                .orElseThrow();
    }

    public void updateLastLogin(UserEntity userEntity) {
        userEntity.setLastLogin(new Date());
        userJpaRepository.save(userEntity);
    }
}
