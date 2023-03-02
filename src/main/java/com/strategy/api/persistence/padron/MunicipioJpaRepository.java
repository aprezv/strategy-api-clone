package com.strategy.api.persistence.padron;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 9/11/22.
 */
public interface MunicipioJpaRepository extends JpaRepository<MunicipioEntity, Integer> {
    List<MunicipioEntity> findByProvinciaId(Integer idProvincia);
}
