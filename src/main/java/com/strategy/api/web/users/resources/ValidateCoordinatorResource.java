package com.strategy.api.web.users.resources;

import com.strategy.api.domain.padron.MilitanteRepository;
import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.register.exceptions.SimpatizanteRegisteredInSeException;
import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.domain.users.exceptions.UserExistException;
import com.strategy.api.web.padron.exceptions.MilitanteNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Created on 6/21/22.
 */
@RestController
@RequestMapping("/validate-coordinator")
public class ValidateCoordinatorResource {
    private final MilitanteRepository militanteRepository;
    private final SimpatizanteRepository simpatizanteRepository;
    private final UserRepository userRepository;

    public ValidateCoordinatorResource(final MilitanteRepository militanteRepository,
                                       final SimpatizanteRepository simpatizanteRepository,
                                       final UserRepository userRepository) {
        this.militanteRepository = militanteRepository;
        this.simpatizanteRepository = simpatizanteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity validate(@RequestParam("cedula") String cedula) {
        if(userRepository.userExist(cedula)) {
            throw new UserExistException();
        }

        if(simpatizanteRepository.existsInSe(cedula)) {
            throw new SimpatizanteRegisteredInSeException();
        }

        Simpatizante simpatizante = simpatizanteRepository.findByCedula(cedula);
        if(simpatizante != null) {
            return ResponseEntity.ok(Map.of("simpatizante", simpatizante ));
        }
        Militante militante = Optional.ofNullable(militanteRepository.findByCedula(cedula))
                .orElseThrow(MilitanteNotFoundException::new);

        return ResponseEntity.ok(Map.of("militante", militante ));
    }
}
