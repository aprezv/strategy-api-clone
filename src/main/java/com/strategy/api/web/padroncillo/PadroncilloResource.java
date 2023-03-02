package com.strategy.api.web.padroncillo;

import com.strategy.api.auth.utils.AuthUtils;
import com.strategy.api.domain.padroncillo.PadroncilloGenerator;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import com.strategy.api.persistence.users.UserEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created on 9/2/22.
 */
@RestController
@RequestMapping("/padroncillo")
public class PadroncilloResource {
    private final PadroncilloGenerator padroncilloGenerator;
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;

    public PadroncilloResource(final PadroncilloGenerator padroncilloGenerator,
                               final SimpatizanteJpaRepository simpatizanteJpaRepository) {
        this.padroncilloGenerator = padroncilloGenerator;
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
    }

    @PostMapping
    public ResponseEntity<Resource> generate(@RequestBody(required = false) GeneratePadroncilloRequest request) throws IOException {

        if(request.getIdSimpatizante() == null) {
            UserEntity currentUser = AuthUtils.getCurrentUser();
            Long idSimpatizante =
                    simpatizanteJpaRepository.findByMilitanteCedula(currentUser.getMilitante().getCedula())
                            .map(SimpatizanteEntity::getId)
                    .orElse(null);
            request.setIdSimpatizante(idSimpatizante);
        }

        Resource resource = padroncilloGenerator.generate(request);

        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(resource);

    }

    @PostMapping("/municipio")
    public ResponseEntity generate2(@RequestBody(required = false) GeneratePadroncilloRequest request) throws IOException {

        Integer count = padroncilloGenerator.generarPorProcinvia(request);


        return ResponseEntity.ok(count);
    }
}
