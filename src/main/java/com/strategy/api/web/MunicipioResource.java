package com.strategy.api.web;

import com.strategy.api.persistence.padron.MunicipioJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 9/11/22.
 */
@RestController
@RequestMapping("/municipios")
public class MunicipioResource {
    private final MunicipioJpaRepository municipioJpaRepository;

    public MunicipioResource(MunicipioJpaRepository municipioJpaRepository) {
        this.municipioJpaRepository = municipioJpaRepository;
    }

    @GetMapping
    ResponseEntity getMunicipios(@RequestParam("idProvincia") Integer idProcinvia) {

        return ResponseEntity.ok(municipioJpaRepository.findByProvinciaId(idProcinvia));
    }
}
