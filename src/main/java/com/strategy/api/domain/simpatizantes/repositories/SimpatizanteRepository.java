package com.strategy.api.domain.simpatizantes.repositories;

import com.strategy.api.domain.simpatizantes.Simpatizante;

/**
 * Created on 6/13/22.
 */
public interface SimpatizanteRepository {
    Simpatizante save(Simpatizante simpatizante);
    Simpatizante findByCedula(String cedula);
    Simpatizante findById(Long id);
    Boolean simpatizanteExiste(String cedula);

    boolean existsInSe(String cedula);
}
