package com.strategy.api.reports;

/**
 * Created on 8/3/22.
 */
public interface SimpatizanteReportResponse {
    String getNombre();
    String getCedula();
    String getTelefono();
    String getWhatsApp();
    String getRole();
    Integer getSimpatizantes_registrados();
    String getCoordinador();
    String getCedula_coordinador();
    String getId_colegio();
    String getCodigo_recinto();
    Boolean getPld();
    String getPlataforma();
    Integer getId_movimiento();
    String getNombre_movimiento();
}
