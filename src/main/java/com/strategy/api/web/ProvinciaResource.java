package com.strategy.api.web;

import com.strategy.api.persistence.padron.ProvinciaJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 9/11/22.
 */
@RestController
@RequestMapping("/provincias")
public class ProvinciaResource {
    private final ProvinciaJpaRepository repository;

    public ProvinciaResource(ProvinciaJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity provincias() {
        return ResponseEntity.ok(
                repository.findAll()
        );

    }
}
