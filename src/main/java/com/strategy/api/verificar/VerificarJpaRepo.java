package com.strategy.api.verificar;

import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created on 9/9/22.
 */
public interface VerificarJpaRepo extends JpaRepository<SimpatizanteEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT\n" +
            "\tp.cedula as \"cedula\",\n" +
            "\tconcat(p.nombres, ' ', p.apellido1, ' ', p.apellido2) as \"nombre\",\n" +
            "\ts.phone_number as \"telefono\",\n" +
            "\ts.whats_app as \"whatsapp\",\n" +
            "\tp.provincia as \"provincia\",\n" +
            "\tp.municipio as \"municipio\",\n" +
            "\tp.distritomunicipal as \"distrito municipal\",\n" +
            "\tp.sectorrecinto as \"sector recinto\",\n" +
            "\tp.codigocircunscripcion as \"circunscripcion\",\n" +
            "\tp.descripcionrecinto as \"recinto\",\n" +
            "\tp.colegio as \"Colegio\",\n" +
            "\tcase when s.is_se is true then 'Sector Externo' else 'Strategy' end as \"plataforma\",\n" +
            "\tcase when p.pld is not NULL then true else false end as \"pld\"\n" +
            "FROM\n" +
            "\tsimpatizantes s\n" +
            "\tINNER JOIN padron p ON p.cedula = s.militante_cedula\n" +
            "\t--inner join se_users u on u.militante_cedula = p.cedula\n" +
            "\tLEFT OUTER JOIN padron_preliminar p2 ON p2.cedula = s.militante_cedula \n" +
            "WHERE\n" +
            "\tp2.cedula IS NULL")
    List<IVerificarResponse> getVerificar();
}
