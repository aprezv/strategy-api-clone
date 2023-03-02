package com.strategy.api.web.padron;

import com.strategy.api.domain.padron.MilitanteRepository;
import com.strategy.api.domain.simpatizantes.exceptions.SimpatizanteRegistradoException;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.web.padron.exceptions.MilitanteNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created on 6/12/22.
 */
@RestController
@RequestMapping("/padron")
public class PadronResource {
    private final MilitanteRepository militanteRepository;
    private final SimpatizanteRepository simpatizanteRepository;

    public PadronResource(final MilitanteRepository militanteRepository,
                          final SimpatizanteRepository simpatizanteRepository) {
        this.militanteRepository = militanteRepository;
        this.simpatizanteRepository = simpatizanteRepository;
    }

    @GetMapping
    public ResponseEntity findByCedula(@RequestParam("cedula") String cedula) {

        Boolean existe = simpatizanteRepository.simpatizanteExiste(cedula);

        if(existe) {
            throw new SimpatizanteRegistradoException();
        }

        return Optional.ofNullable(militanteRepository.findByCedula(cedula))
                .map(ResponseEntity::ok)
                .orElseThrow(MilitanteNotFoundException::new);
    }
}
