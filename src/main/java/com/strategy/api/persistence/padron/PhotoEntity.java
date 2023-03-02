package com.strategy.api.persistence.padron;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 6/14/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Subselect("select * from fotos_pld_pld")
public class PhotoEntity {

    @Id
    @Column(name = "cedula")
    private String cedula;

    @Column(name = "imagen", columnDefinition = "BYTEA")
    private byte[] imagen;

    @Column(name = "verificador")
    private Integer  verificador;

}
