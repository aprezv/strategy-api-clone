package com.strategy.api.persistence.respositories;

import com.strategy.api.persistence.padron.MilitanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created on 6/12/22.
 */
public interface MilitanteJpaRepository extends JpaRepository<MilitanteEntity, Long> {
    Optional<MilitanteEntity> findByCedula(String cedula);
    Boolean existsMilitanteEntityByCedula(String cedula);
}
