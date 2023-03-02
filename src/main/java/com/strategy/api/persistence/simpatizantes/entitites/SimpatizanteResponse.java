package com.strategy.api.persistence.simpatizantes.entitites;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created on 6/16/22.
 */
public interface SimpatizanteResponse {
    String getCedula();

    String getApellido1();

    String getApellido2();

    String getSexo();

    String getMunicipio();

    String getProvincia();

    String getPhoneNumber();

    String getWhatsApp();

    String getNombres();

    String getMesa();

    String getRecinto();

    Boolean getPld();
}
