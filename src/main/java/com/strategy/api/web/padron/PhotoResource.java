package com.strategy.api.web.padron;

import com.strategy.api.persistence.respositories.PhotoJpaRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created on 6/14/22.
 */
@RestController
@RequestMapping("/photo")
public class PhotoResource {
    private final PhotoJpaRepository repository;

    public PhotoResource(PhotoJpaRepository repository) {
        this.repository = repository;
    }


    @GetMapping(value = "/{cedula}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity getPhoto(@PathVariable("cedula") String cedula) throws IOException {

        return repository.findById(cedula)
                .map(e -> ResponseEntity.ok()
                        .contentLength(e.getImagen().length)
                        .contentType(MediaType.IMAGE_JPEG)
                        .body( new ByteArrayResource(e.getImagen()))
                )
                .orElse(
                        ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(new ByteArrayResource(new ClassPathResource("images/nd.png").getInputStream().readAllBytes()))
                );

    }
}
