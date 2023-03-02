package com.strategy.api.web.simpatizantes;

import com.strategy.api.domain.register.RegisterService;
import com.strategy.api.domain.users.User;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteResponse;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import com.strategy.api.web.register.dto.EditRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 6/13/22.
 */
@RestController
@RequestMapping("/simpatizantes")
public class SimpatizantesSearchResource  {
    private final SimpatizanteJpaRepository repository;
    private final RegisterService registerService;

    public SimpatizantesSearchResource(final SimpatizanteJpaRepository repository, RegisterService registerService) {
        this.repository = repository;
        this.registerService = registerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page search(@RequestParam(name = "cedula", required = false, defaultValue = "") final String cedula,
                       @RequestParam(name = "nombre", required = false, defaultValue = "") final String nombre,
                       @RequestParam(name = "registeredById", required = false, defaultValue = "") final Long registetedById,
                       @RequestParam(name = "role", required = false, defaultValue = "") final User.Role role,
                       @RequestParam(name = "_page", required = false, defaultValue = "0") final Integer page,
                       @RequestParam(name = "_size", required = false, defaultValue = "25") final Integer size,
                       @RequestParam(name = "_sort", required = false, defaultValue = "") String sort
    ) {

        Long parentId = registerService.getParentId();

        //return repository.getSimpatizantes(cedula, parentId, registetedById, role, PageRequest.of(page, size));
        return repository.getSimpatizantes(cedula, nombre, parentId, registetedById, role, PageRequest.of(page, size));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        SimpatizanteResponse res = repository.getSimpatizanteById(id);
        return ResponseEntity.ok(res);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        registerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") Long id, @RequestBody EditRequest request) {
        request.setId(id);
        registerService.edit(request);
        return ResponseEntity.ok().build();
    }
}
