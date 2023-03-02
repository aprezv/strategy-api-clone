package com.strategy.api.domain.padron.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 6/12/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Militante {
    private String cedula;
    private String nombres;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private Boolean pld;

    @JsonIgnore
    public String getFullName() {
        return String.format("%s %s %s", nombres, apellido1, apellido2);
    }
}
