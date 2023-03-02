package com.strategy.api.domain.padron;

import com.strategy.api.domain.padron.model.Militante;

/**
 * Created on 6/12/22.
 */
public interface MilitanteRepository {
    Militante findByCedula(String cedula);
    Boolean militanteExists(String cedula);
}
