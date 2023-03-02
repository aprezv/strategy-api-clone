package com.strategy.api.persistence.simpatizantes.entitites;

/**
 * Created on 6/21/22.
 */
public interface SimpatizanteListResponse {
    String getPhone_number();
    String getWhats_app();
    String getSimpatizante_nombre();
    String getSimpatizante_cedula();
    String getRegistrado_nombre();
    String getRegistrado_Cedula();
    Long getRegistrado_id();
    Long getSimpatizante_id();
    Boolean getPld();
    String getUser_id();
    String getUser_role();
    Long getRegistered_count();
    Boolean  getVoto_centro();
    Double getLat();
    Double getLng();
}
