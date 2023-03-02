package com.strategy.api.web.register;

import com.strategy.api.domain.register.RegisterService;
import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.web.register.dto.ReassignRequest;
import com.strategy.api.web.register.exceptions.CoordinatorNotExistsException;
import com.strategy.api.web.register.exceptions.SympathizerNotRegisteredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Created on 6/23/22.
 */
@RestController
@RequestMapping("/reassign")
public class ReassignResource {
    private final SimpatizanteRepository simpatizanteRepository;
    private final RegisterService registerService;
    private final UserRepository userRepository;

    public ReassignResource(final SimpatizanteRepository simpatizanteRepository,
                            final RegisterService registerService,
                            final UserRepository userRepository) {
        this.simpatizanteRepository = simpatizanteRepository;
        this.registerService = registerService;
        this.userRepository = userRepository;
    }


    @PostMapping
    ResponseEntity reAssign(@RequestBody ReassignRequest request) {
        registerService.reassign(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-sympathizer/{cedula}")
    ResponseEntity validateSimpatizante(@PathVariable("cedula") String cedula) {

        Simpatizante simpatizante = Optional.ofNullable(simpatizanteRepository.findByCedula(cedula))
                .orElseThrow(SympathizerNotRegisteredException::new);

        return ResponseEntity.ok(
                Map.of(
                        "nombre", simpatizante.getMilitante().getFullName(),
                        "cedula", simpatizante.getMilitante().getCedula(),
                        "id", simpatizante.getId()
                        )
        );

    }

    @GetMapping("/validate-coordinator/{cedula}")
    ResponseEntity validateCoordinator(@PathVariable("cedula") String cedula) {

        User coordinador = Optional.ofNullable(userRepository.findByCedula(cedula))
                .orElseThrow(CoordinatorNotExistsException::new);

        return ResponseEntity.ok(
                Map.of(
                        "nombre", coordinador.getMilitante().getFullName(),
                        "cedula", coordinador.getMilitante().getCedula(),
                        "id", coordinador.getId(),
                        "role", coordinador.getRole()
                )
        );

    }
}
