package com.strategy.api.web.register;

import com.strategy.api.domain.register.RegisterService;
import com.strategy.api.domain.simpatizantes.exceptions.SimpatizanteRegistradoException;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import com.strategy.api.web.register.dto.RegisterRequest;
import com.strategy.api.web.register.exceptions.CoordinatorNotExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created on 6/13/22.
 */
@RestController
@RequestMapping("/register")
public class RegisterResource {
    private final RegisterService registerService;
    private final SimpatizanteRepository simpatizanteRepository;
    private final UserRepository userRepository;
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;

    public RegisterResource(final RegisterService registerService,
                            final SimpatizanteRepository simpatizanteRepository,
                            final UserRepository userRepository,
                            final SimpatizanteJpaRepository simpatizanteJpaRepository) {
        this.registerService = registerService;
        this.simpatizanteRepository = simpatizanteRepository;
        this.userRepository = userRepository;
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
    }

    @PostMapping
    ResponseEntity register(@RequestBody RegisterRequest request) {
        Boolean existe = simpatizanteRepository.simpatizanteExiste(request.getCedula());

        if(existe) {
            throw new SimpatizanteRegistradoException();
        }
        registerService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/third")
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    ResponseEntity registerThird(@RequestBody RegisterRequest request) {
        Boolean existe = simpatizanteRepository.simpatizanteExiste(request.getCedula());

        if(existe) {
            throw new SimpatizanteRegistradoException();
        }

        User user = Optional.ofNullable(userRepository.findById(request.getUserId()))
                .orElseThrow(CoordinatorNotExistsException::new);


        SimpatizanteEntity registeredBy = simpatizanteJpaRepository.findByMilitanteCedula(user.getMilitante().getCedula())
                .orElseThrow(CoordinatorNotExistsException::new);

        request.setRegisteredById(registeredBy);
        registerService.register(request);
        return ResponseEntity.ok().build();
    }
}
