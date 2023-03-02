package com.strategy.api.persistence.simpatizantes.entitites;

/**
 * Created on 6/21/22.
 */
public interface SimpatizanteExternalResponse {
    String getCedula();
    String getTelefono();
    String getWhatsapp();
    String getRol();
    String getSimpatizantes_registrados();
    String getCoordinador();
    String getCedula_coordinador();
    Boolean getPld();
    String getPlataforma();
    String getCodigo_recinto();
    String getId_colegio();
}
