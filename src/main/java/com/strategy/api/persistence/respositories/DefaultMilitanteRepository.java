package com.strategy.api.persistence.respositories;

import com.strategy.api.domain.padron.MilitanteRepository;
import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.persistence.padron.MilitanteJpaConverter;
import com.strategy.api.web.padron.exceptions.MilitanteNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Created on 6/12/22.
 */
@Repository
public class DefaultMilitanteRepository implements MilitanteRepository {
    private final MilitanteJpaRepository jpaRepo;
    private final MilitanteJpaConverter converter;

    public DefaultMilitanteRepository(final MilitanteJpaRepository jpaRepo,
                                      final MilitanteJpaConverter converter) {
        this.jpaRepo = jpaRepo;
        this.converter = converter;
    }

    @Override
    public Militante findByCedula(String cedula) {
        return jpaRepo.findByCedula(cedula)
                .map(converter::fromJpaEntity)
                .orElseThrow(MilitanteNotFoundException::new);
    }

    @Override
    public Boolean militanteExists(String cedula) {
        return jpaRepo.existsMilitanteEntityByCedula(cedula);
    }

}
