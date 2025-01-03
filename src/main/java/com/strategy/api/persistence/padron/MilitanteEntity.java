package com.strategy.api.persistence.padron;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Subselect("SELECT * FROM padron")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilitanteEntity {
    @ManyToOne
    @JoinColumn(name = "idmunicipio", updatable = false, insertable = false)
    private MunicipioEntity municipioEntity;

    @Id
    @Column(name = "cedula", updatable = false, insertable = false)
    private String cedula;

    @Column(name = "nombres", updatable = false, insertable = false)
    private String nombres;

    @Column(name = "apellido1", updatable = false, insertable = false)
    private String apellido1;

    @Column(name = "apellido2", updatable = false, insertable = false)
    private String apellido2;

    @Column(name = "idsexo", updatable = false, insertable = false)
    private String sexo;

    @Column(name = "pld", updatable = false, insertable = false)
    private String pld;

    @JsonIgnore
    public String getFullName() {
        return String.format("%s %s %s", nombres, apellido1, apellido2);
    }
}


/*
 [IdProvincia]
         ,[IdMunicipio]
         ,[CodigoCircunscripcion]
         ,[CodigoRecinto]
         ,[CodigoColegio]
         ,[Cedula]
         ,[nombres]
         ,[apellido1]
         ,[apellido2]
         ,[NombresPlastico]
         ,[ApellidosPlastico]
         ,[FechaNacimiento]
         ,[IdNacionalidad]
         ,[IdSexo]
         ,[IdEstadoCivil]
         ,[IdCategoria]
         ,[IdCausaCancelacion]
         ,[IdColegio]
         ,[IdColegioOrigen]
         ,[IdMunicipioOrigen]
         ,[ColegioOrigen]
         ,[PosPagina]
         ,[LugarVotacion]
         ,[IdProvinciaExterior]
         ,[IdMunicipioExterior]
         ,[CodigoRecintoExterior]
         ,[ColegioExterior]
         ,[PosPaginaExterior]
         ,[Pagina]
         ,[IdRangoEdad] */
