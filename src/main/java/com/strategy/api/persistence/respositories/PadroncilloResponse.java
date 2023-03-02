package com.strategy.api.persistence.respositories;

import javax.persistence.Lob;

/**
 * Created on 9/2/22.
 */
public interface PadroncilloResponse {
    String getCedula();
    String getNombre();
    String getRecinto();
    String getColegio();
    String getTelefono();
    String getWhats_app();
    String getDireccion();
    String getSector();
    String getProvincia();
    String getMunicipio();
    String getCoordinador();
    String getCedula_coordinador();
    String getTelefono_coordinador();
    String getWhatsapp_coordinador();
    String getCirc();
    Boolean getPld();
    @Lob
    byte[] getFoto();
}
