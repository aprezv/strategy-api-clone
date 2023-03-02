package com.strategy.api.persistence.respositories;

import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 9/2/22.
 */
@Repository
public interface PadroncilloJpaRepository extends JpaRepository<SimpatizanteEntity, Long> {

    // consulta con padron consulta final
/*    @Query(nativeQuery = true, value = "SELECT " +
            " concat(p.nombres, ' ', p.apellido1, ' ', p.apellido2) as nombre, " +
            " p.cedula as cedula,  " +
            " p.recintomadre as recinto, " +
            " p.direccionrecintomadre as direccion," +
            " p.sectorrecintomadre as sector," +
            " p.municipio, " +
            " p.provincia, " +
            " p.mesa as colegio, " +
            " s.phone_number as telefono, " +
            " s.whats_app as whats_app, " +
            " f.imagen as foto, " +
            " c.call_status, " +
            " c.call_response, " +
            " case when p2.pld is null then false else true end  as pld " +
            "FROM " +
            " simpatizantes s " +
            " INNER JOIN padronconsulta_final p ON p.cedula = s.militante_cedula " +
            " left join padron p2 on p2.cedula = s.militante_cedula " +
            " inner join fotos_pld_pld f on f.cedula = s.militante_cedula " +
            " left join call_details c on c.id = s.id " +
            " left join users u on u.militante_cedula = s.militante_cedula " +
            " left join se_users u2 on u2.militante_cedula = s.militante_cedula " +
            " where s.registered_by_id = :id  " +
            " and u.id is NULL and u2.id is null")
    List<PadroncilloResponse> getPadroncillo(@Param("id") Long registeredById);*/

    @Query(nativeQuery = true, value = "SELECT " +
            " concat(p.nombres, ' ', p.apellido1, ' ', p.apellido2) as nombre, " +
            " p.cedula as cedula,  " +
            " p.descripcionrecinto as recinto, " +
            " p.direccionrecinto as direccion," +
            " p.sectorrecinto as sector," +
            " p.municipio, " +
            " p.provincia, " +
            " p.colegio as colegio, " +
            " s.phone_number as telefono, " +
            " s.whats_app as whats_app, " +
            " f.imagen as foto, " +
            " c.call_status, " +
            " c.call_response, " +
            " case when p.pld is null then false else true end  as pld " +
            "FROM " +
            " simpatizantes s " +
            " INNER JOIN padron p ON p.cedula = s.militante_cedula " +
            " inner join fotos_pld_pld f on f.cedula = s.militante_cedula " +
            " left join call_details c on c.id = s.id " +
            " left join users u on u.militante_cedula = s.militante_cedula " +
            " where s.registered_by_id = :id  " +
            " and u.id is NULL")
    List<PadroncilloResponse> getPadroncillo(@Param("id") Long registeredById);



    //Query a padron consulta final
    /*@Query(nativeQuery = true, value = "SELECT " +
            " concat(p2.nombres, ' ', p2.apellido1, ' ', p2.apellido2) as nombre, " +
            " p2.cedula as cedula,  " +
            " case when p.recintomadre is null then 'NO HABILITADO PARA CONSULTA' else p.recintomadre end as recinto, " +
            " p.mesa as colegio, " +
            " p.direccionrecintomadre as direccion," +
            " p.sectorrecintomadre as sector," +
            " p.municipio, " +
            " p.provincia, " +
            " p2.codigocircunscripcion as circ, " +
            " s.phone_number as telefono, " +
            " s.whats_app as whats_app, " +
            " f.imagen as foto, " +
            " c.call_status, " +
            " c.call_response, " +
            " case when p2.pld is null then false else true end  as pld, " +
            " concat(p3.nombres, ' ', p3.apellido1, ' ', p3.apellido2) as coordinador,\n" +
            " p3.cedula as cedula_coordinador,\n" +
            " s2.phone_number as telefono_coordinador,\n" +
            " s2.whats_app as whatsapp_coordinador " +
            "FROM " +
            " simpatizantes s " +
            " INNER JOIN padron p2 ON p2.cedula = s.militante_cedula " +
            " left JOIN padronconsulta_final p ON p.cedula = s.militante_cedula " +
            " inner join fotos_pld_pld f on f.cedula = s.militante_cedula " +
            " left join call_details c on c.id = s.id " +
            " LEFT join simpatizantes s2 on s2.id = s.registered_by_id\n" +
            " left join padron p3 on p3.cedula = s2.militante_cedula " +
            " where s.id = :id")
    PadroncilloResponse getSimpatizante(@Param("id") Long id);*/

    @Query(nativeQuery = true, value = "SELECT " +
            " concat(p.nombres, ' ', p.apellido1, ' ', p.apellido2) as nombre, " +
            " p.cedula as cedula,  " +
            " p.descripcionrecinto as recinto, " +
            " p.colegio as colegio, " +
            " p.direccionrecinto as direccion," +
            " p.sectorrecinto as sector," +
            " p.municipio, " +
            " p.provincia, " +
            " p.codigocircunscripcion as circ, " +
            " s.phone_number as telefono, " +
            " s.whats_app as whats_app, " +
            " f.imagen as foto, " +
            " c.call_status, " +
            " c.call_response, " +
            " case when p.pld is null then false else true end  as pld, " +
            " concat(p3.nombres, ' ', p3.apellido1, ' ', p3.apellido2) as coordinador,\n" +
            " p3.cedula as cedula_coordinador,\n" +
            " s2.phone_number as telefono_coordinador,\n" +
            " s2.whats_app as whatsapp_coordinador " +
            "FROM " +
            " simpatizantes s " +
            " INNER JOIN padron p ON p.cedula = s.militante_cedula " +
            " inner join fotos_pld_pld f on f.cedula = s.militante_cedula " +
            " left join call_details c on c.id = s.id " +
            " LEFT join simpatizantes s2 on s2.id = s.registered_by_id\n" +
            " left join padron p3 on p3.cedula = s2.militante_cedula " +
            " where s.id = :id")
    PadroncilloResponse getSimpatizante(@Param("id") Long id);


    @Query(nativeQuery = true, value = "select count(1) from users u join padron p on u.militante_cedula = p.cedula where p.idmunicipio = 31 and registered_count > 0 ")
    Integer countByMunicipio(Long idMunicipio);


    @Query(nativeQuery = true, value = "select s.id from users u join simpatizantes s on s.militante_cedula = u.militante_cedula join padron p on u.militante_cedula = p.cedula where p.idmunicipio = :id and registered_count > 0")
    List<Long> getIdSimpatizantes(@Param("id") Long idMunicipi0);
}
