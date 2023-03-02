package com.strategy.api.verificar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 9/9/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificarResponse implements IVerificarResponse{
    private String cedula;
    private String nombre;
    private String telefono;
    private String whatsapp;
    private String municipio;
    private String provincia;
    private String distrito_municipal;
    private String cirscunscripcion;
    private String plataforma;
    private String pld;
    private String colegio;
    private String sector_recinto;
    private Boolean verificate;
}
