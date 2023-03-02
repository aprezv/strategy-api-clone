package com.strategy.api.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 9/5/22.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpatizanteDto {
    private String Cedula;
    private String Telefono;
    private String WhatsApp;
    private String CedulaCoordinador;
    private String Rol;
    private String Simpatizantes;
    private String PLD;
    private String Plataforma;
    private String IDColegio;
    private String CodigoRecinto;
}
