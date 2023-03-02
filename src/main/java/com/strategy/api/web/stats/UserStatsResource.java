package com.strategy.api.web.stats;

import com.strategy.api.domain.register.RegisterService;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created on 6/26/22.
 */
@RestController
@RequestMapping("/stats")
public class UserStatsResource {
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;
    private final RegisterService registerService;

    public UserStatsResource(final SimpatizanteJpaRepository simpatizanteJpaRepository, RegisterService registerService) {
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
        this.registerService = registerService;
    }

    @GetMapping
    ResponseEntity getStats() {

        Long parentId = registerService.getParentId();

        Integer simpatizantesDirectos = simpatizanteJpaRepository.countAllByRegisteredById(parentId);
        Integer totalSimpatizants = simpatizanteJpaRepository.countAll(parentId);
        Integer coordinadores = simpatizanteJpaRepository.countCoordinators(parentId);

        return ResponseEntity.ok(
                Map.of(
                        "simpatizantesDirectos", simpatizantesDirectos,
                        "totalSimpatizantes", totalSimpatizants,
                        "coordinadores", coordinadores
                )
        );
    }
}
