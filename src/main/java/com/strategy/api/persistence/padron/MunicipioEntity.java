package com.strategy.api.persistence.padron;

import lombok.Data;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created on 6/12/22.
 */
@Data
@Entity
@Subselect("SELECT * FROM municipio")
public class MunicipioEntity {

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Integer id;

    @Column(name = "descripcion", updatable = false, insertable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idprovincia", updatable = false, insertable = false)
    private Provincia provincia;
}

         /* ,[Descripcion]
                  ,[IDProvincia]
                  ,[IDMunicipioPadre]
                  ,[Oficio]
                  ,[Estatus]
                  ,[DM]
                  ,[IdUsuarioCreacion]
                  ,[FechaCreacion]
                  ,[IdUsuarioModificacion]
                  ,[FechaModificacion]
                  ,[RegID]
                  ,[TipoFormaVotacion] */
