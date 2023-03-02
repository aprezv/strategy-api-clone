package com.strategy.api.domain.users;

import com.strategy.api.domain.padron.MilitanteRepository;
import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.register.RegisterService;
import com.strategy.api.domain.users.exceptions.UserExistException;
import com.strategy.api.events.SympathizerAddedEvent;
import com.strategy.api.persistence.padron.MilitanteEntity;
import com.strategy.api.web.register.dto.RegisterRequest;
import com.strategy.api.web.users.dto.PromoteRequest;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 6/14/22.
 */
@Service
public class PromoteService {
    private final UserRepository userRepository;
    private final MilitanteRepository militanteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterService registerService;
    private final ApplicationEventPublisher eventPublisher;

    public PromoteService(final UserRepository userRepository,
                          final MilitanteRepository militanteRepository,
                          final PasswordEncoder passwordEncoder,
                          final RegisterService registerService,
                          final ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.militanteRepository = militanteRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerService = registerService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void promote(PromoteRequest request) {
        if(userRepository.userExist(request.getCedula())) {
            throw new UserExistException();
        }

        if(!request.getIsSimpatizante()) {
            registerService.register(
                    RegisterRequest.builder()
                            .cedula(request.getCedula())
                            .whatsApp(request.getWhatsApp())
                            .phone(request.getPhone())
                            .build()
            );
        }

        Militante militante = militanteRepository.findByCedula(request.getCedula());
        String password = getPassword(militante);
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .role(BooleanUtils.isTrue(request.getAllowCreateCoordinators()) ? User.Role.COORDINADOR : User.Role.MULTIPLICADOR)
                .username(militante.getCedula())
                .name(militante.getFullName())
                .password(encodedPassword)
                .militante(Militante.builder().cedula(militante.getCedula()).build())
                .build();

        userRepository.save(user);

        eventPublisher.publishEvent(
                SympathizerAddedEvent.builder()
                        .source(this)
                        .cedula(request.getCedula())
                        .build());
    }

    public String getPassword(Militante militante) {

        return String.format(
                "%s%s%s",
                militante.getNombres().substring(0,1).toUpperCase(),
                militante.getApellido1().substring(0,1).toLowerCase(),
                militante.getCedula().substring(militante.getCedula().length() - 4)
        );

    }

    public String getPassword(MilitanteEntity militante) {

        return String.format(
                "%s%s%s",
                militante.getNombres().substring(0,1).toUpperCase(),
                militante.getApellido1().substring(0,1).toLowerCase(),
                militante.getCedula().substring(militante.getCedula().length() - 4)
        );

    }
}
