package com.strategy.api.persistence.simpatizantes.repositories;

import com.strategy.api.base.repositories.ExtendedRepository;
import com.strategy.api.domain.users.User;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteExternalResponse;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteListResponse;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteResponse;
import com.strategy.api.reports.ReporteProvincia;
import com.strategy.api.reports.SimpatizanteReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created on 6/13/22.
 */
public interface SimpatizanteJpaRepository extends ExtendedRepository<SimpatizanteEntity, Long> {
    Boolean existsSimpatizanteEntityByMilitanteCedula(String cedula);

    Boolean existsSimpatizanteEntityByMilitanteCedulaAndIsSeIsTrue(String cedula);

    Integer countAllByRegisteredById(Long registeredById);

    @Query(nativeQuery = true, value = "select s.id from simpatizantes s join users u on u.militante_cedula = s.militante_cedula where u.id = :userId and s.is_se is not true")
    Optional<Long> getIdByUser(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select u.id from simpatizantes s join users u on u.militante_cedula = s.militante_cedula where s.id = :id and s.is_se is not true")
    Optional<Long> getUserIdById(@Param("id") Long userId);

    Optional<SimpatizanteEntity> findByMilitanteCedula(String cedula);

    /// Query al padron consulta final
/*    @Query(nativeQuery = true, value = "SELECT\n" +
            "  s.phone_number as phoneNumber,\n" +
            "  s.whats_app as whatsApp,\n" +
            "  c.cedula,\n" +
            "  c.nombres,\n" +
            "  c.apellido1,\n" +
            "  c.apellido2,\n" +
            "  p.provincia as provincia,\n" +
            "  p.municipio as municipio,\n" +
            "  p.mesa as mesa,\n" +
            "  p.recintomadre as recinto,\n" +
            "  case when pld is null then false else true end  as pld\n" +
            "FROM\n" +
            "  simpatizantes s\n" +
            "    inner join padron c ON s.militante_cedula = c.cedula\n" +
            "    left join padronconsulta_final p ON s.militante_cedula = p.cedula\n" +
            "WHERE\n" +
            "    s.id=?1 and is_se is not true")
    SimpatizanteResponse getSimpatizanteById(@Param("id") Long id);*/

    @Query(nativeQuery = true, value = "SELECT\n" +
            "  s.phone_number as phoneNumber,\n" +
            "  s.whats_app as whatsApp,\n" +
            "  c.cedula,\n" +
            "  c.nombres,\n" +
            "  c.apellido1,\n" +
            "  c.apellido2,\n" +
            "  c.provincia as provincia,\n" +
            "  c.municipio as municipio,\n" +
            "  null as mesa,\n" +
            "  c.descripcionrecinto as recinto,\n" +
            "  case when pld is null then false else true end  as pld\n" +
            "FROM\n" +
            "  simpatizantes s\n" +
            "    inner join padron c ON s.militante_cedula = c.cedula\n" +
            "WHERE\n" +
            "    s.id=?1 and is_se is not true")
    SimpatizanteResponse getSimpatizanteById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "concat(p.nombres, ' ', p.apellido1,' ', p.apellido2) as \"nombre\",\n" +
            " s.militante_cedula as \"cedula\", \n" +
            " s.phone_number as \"telefono\", \n" +
            " s.whats_app as \"whatsapp\",\n" +
            " u.role as \"rol\",\n" +
            " u.registered_count \"simpatizantes_registrados\",\n" +
            " concat(p2.nombres, ' ', p2.apellido1,' ', p2.apellido2) as \"coordinador\",\n" +
            "  s2.militante_cedula as \"cedula_coordinador\",\n" +
            "  case when p.pld is null then false else true end  as pld,\n" +
            "  case when s.is_se is null then 'Strategy' else 'Sector Externo' end  as \"plataforma\"\n," +
            "  p.codigorecinto as \"codigo_recinto\",\n" +
            "  p.idcolegio as \"id_colegio\" " +
            "FROM  simpatizantes s\n" +
            "  INNER JOIN padron P ON P.cedula = s.militante_cedula\n" +
            "  left JOIN simpatizantes s2 ON s2.ID = s.registered_by_id\n" +
            "  left JOIN padron p2 ON p2.cedula = s2.militante_cedula\n" +
            "  left JOIN users u ON u.militante_cedula = s.militante_cedula where s.militante_cedula = :cedula")
    SimpatizanteExternalResponse getForExternalPlatform(@Param("cedula") String cedula);


    @Query(nativeQuery = true, value = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app,lat,lng, registered_by_id, PATH ) AS (\n" +
            "  SELECT ID,\n" +
            "         militante_cedula,\n" +
            "         phone_number,\n" +
            "         whats_app," +
            "         lat, " +
            "         lng, " +
            "         registered_by_id,\n" +
            "         ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "      s.is_se is not true " +
            "    AND\n" +
            "    CASE\n" +
            "      WHEN :parentId IS NULL THEN\n" +
            "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
            "      END \n" +
            "\tUNION ALL\n" +
            "  SELECT\n" +
            "    s.ID,\n" +
            "    s.militante_cedula,\n" +
            "    s.phone_number,\n" +
            "    s.whats_app,\n" +
            "    s.lat,\n" +
            "    s.lng,\n" +
            "    s.registered_by_id,\n" +
            "    r.PATH || s.ID AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
            "  WHERE\n" +
            "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
            ") SELECT \n" +
            " a.id as simpatizante_id, \n" +
            " a.militante_cedula as simpatizante_cedula, \n" +
            " a.phone_number, \n" +
            " a.whats_app,\n" +
            " a.lat," +
            " a.lng," +
            " u.role as user_role,\n" +
            " u.id as user_id,\n" +
            " u.registered_count,\n" +
            " concat(p.nombres, ' ', p.apellido1,' ', p.apellido2) as simpatizante_nombre,\n" +
            " concat(p2.nombres, ' ', p2.apellido1,' ', p2.apellido2) as registrado_nombre,\n" +
            " s2.militante_cedula as registrado_cedula,\n" +
            " s2.id as registrado_id," +
            " s1.voto_centro, \n" +
            "  case when p.pld is null then false else true end  as pld\n" +
            "FROM\n" +
            "  simpatizantes_r A\n" +
            "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
            "  INNER JOIN padron P ON P.cedula = s1.militante_cedula\n" +
            "  left JOIN simpatizantes s2 ON s2.ID = A.registered_by_id\n" +
            "  left JOIN padron p2 ON p2.cedula = s2.militante_cedula\n" +
            "  left JOIN users u ON u.militante_cedula = s1.militante_cedula\n" +
            "      where ((:nombre = '' or concat ( P.nombres, ' ', P.apellido1, ' ', P.apellido2 ) ilike concat('%',:nombre,'%'))\n" +
            "      or (:cedula = ''  or p.cedula like concat('%',:cedula,'%'))) and (:registeredById is null or CAST(CAST(:registeredById AS TEXT) AS BIGINT) = A.registered_by_id)  and s1.is_se is not true and  (:role is null or  u.role in ('COORDINADOR','MULTIPLICADOR'))" +
            "\t\t\t",
            countQuery = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
                    "  SELECT ID,\n" +
                    "         militante_cedula,\n" +
                    "         phone_number,\n" +
                    "         whats_app,\n" +
                    "         registered_by_id,\n" +
                    "         ARRAY [ ID ] AS PATH\n" +
                    "  FROM\n" +
                    "    simpatizantes s\n" +
                    "  WHERE\n" +
                    "      s.is_se is not true " +
                    "    AND\n" +
                    "    CASE\n" +
                    "      WHEN :parentId IS NULL THEN\n" +
                    "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
                    "      END \n" +
                    "\tUNION ALL\n" +
                    "  SELECT\n" +
                    "    s.ID,\n" +
                    "    s.militante_cedula,\n" +
                    "    s.phone_number,\n" +
                    "    s.whats_app,\n" +
                    "    s.registered_by_id,\n" +
                    "    r.PATH || s.ID AS PATH\n" +
                    "  FROM\n" +
                    "    simpatizantes s\n" +
                    "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
                    "  WHERE\n" +
                    "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
                    ") SELECT count(1)\n" +
                    "FROM\n" +
                    "  simpatizantes_r A \n" +
                    "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
                    "  INNER JOIN padron P ON P.cedula = s1.militante_cedula\n" +
                    "  left JOIN users u ON u.militante_cedula = s1.militante_cedula\n" +
                    "      where ((:nombre = '' or concat ( P.nombres, ' ', P.apellido1, ' ', P.apellido2 ) ilike concat('%',:nombre,'%'))\n" +
                    "      or (:cedula = ''  or p.cedula like concat('%',:cedula,'%'))) and (:registeredById is null or CAST(CAST(:registeredById AS TEXT) AS BIGINT) = s1.registered_by_id) and s1.is_se is not true and  (:role is null or  u.role in ('COORDINADOR','MULTIPLICADOR'))"
    )
    Page<SimpatizanteListResponse> getSimpatizantes(@Param("cedula") String cedula,
                                                    @Param("nombre") String nombre,
                                                    @Param("parentId") Long parentId,
                                                    @Param("registeredById") Long registeredBy,
                                                    @Param("role") User.Role role,
                                                    @Param("pageable")Pageable pageable
                                                    );

    @Query(nativeQuery = true, value = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
            "  SELECT ID,\n" +
            "         militante_cedula,\n" +
            "         phone_number,\n" +
            "         whats_app,\n" +
            "         registered_by_id,\n" +
            "         ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "      s.is_se is not true " +
            "    AND\n" +
            "    CASE\n" +
            "      WHEN :parentId IS NULL THEN\n" +
            "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
            "      END \n" +
            "\tUNION ALL\n" +
            "  SELECT\n" +
            "    s.ID,\n" +
            "    s.militante_cedula,\n" +
            "    s.phone_number,\n" +
            "    s.whats_app,\n" +
            "    s.registered_by_id,\n" +
            "    r.PATH || s.ID AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
            "  WHERE\n" +
            "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
            ") SELECT \n" +
            " a.id as simpatizante_id, \n" +
            " a.militante_cedula as simpatizante_cedula, \n" +
            " a.phone_number, \n" +
            " a.whats_app,\n" +
            " u.role as user_role,\n" +
            " u.id as user_id,\n" +
            " u.registered_count,\n" +
            " concat(p.nombres, ' ', p.apellido1,' ', p.apellido2) as simpatizante_nombre,\n" +
            " concat(p2.nombres, ' ', p2.apellido1,' ', p2.apellido2) as registrado_nombre,\n" +
            " s2.militante_cedula as registrado_cedula,\n" +
            " s2.id as registrado_id,\n" +
            "  case when p.pld is null then false else true end  as pld\n" +
            "FROM\n" +
            "  simpatizantes_r A\n" +
            "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
            "  INNER JOIN padron P ON P.cedula = s1.militante_cedula\n" +
            "  left JOIN simpatizantes s2 ON s2.ID = A.registered_by_id\n" +
            "  left JOIN padron p2 ON p2.cedula = s2.militante_cedula\n" +
            "  left JOIN users u ON u.militante_cedula = s1.militante_cedula\n" +
            "      where (:cedula = ''  or p.cedula = :cedula) and (:registeredById is null or CAST(CAST(:registeredById AS TEXT) AS BIGINT) = A.registered_by_id)  and s1.is_se is not true and  (:role is null or  u.role in ('COORDINADOR','MULTIPLICADOR'))" +
            "\t\t\t",
            countQuery = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
                    "  SELECT ID,\n" +
                    "         militante_cedula,\n" +
                    "         phone_number,\n" +
                    "         whats_app,\n" +
                    "         registered_by_id,\n" +
                    "         ARRAY [ ID ] AS PATH\n" +
                    "  FROM\n" +
                    "    simpatizantes s\n" +
                    "  WHERE\n" +
                    "      s.is_se is not true " +
                    "    AND\n" +
                    "    CASE\n" +
                    "      WHEN :parentId IS NULL THEN\n" +
                    "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
                    "      END \n" +
                    "\tUNION ALL\n" +
                    "  SELECT\n" +
                    "    s.ID,\n" +
                    "    s.militante_cedula,\n" +
                    "    s.phone_number,\n" +
                    "    s.whats_app,\n" +
                    "    s.registered_by_id,\n" +
                    "    r.PATH || s.ID AS PATH\n" +
                    "  FROM\n" +
                    "    simpatizantes s\n" +
                    "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
                    "  WHERE\n" +
                    "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
                    ") SELECT count(1)\n" +
                    "FROM\n" +
                    "  simpatizantes_r A \n" +
                    "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
                    "  INNER JOIN padron P ON P.cedula = s1.militante_cedula\n" +
                    "  left JOIN users u ON u.militante_cedula = s1.militante_cedula\n" +
                    "      where (:cedula = ''  or p.cedula = cedula) and (:registeredById is null or CAST(CAST(:registeredById AS TEXT) AS BIGINT) = s1.registered_by_id) and s1.is_se is not true and  (:role is null or  u.role in ('COORDINADOR','MULTIPLICADOR'))"
    )
    Page<SimpatizanteListResponse> getSimpatizantes(@Param("cedula") String cedula,
                                                    @Param("parentId") Long parentId,
                                                    @Param("registeredById") Long registeredBy,
                                                    @Param("role") User.Role role,
                                                    @Param("pageable")Pageable pageable
    );

    @Query(nativeQuery = true, value = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
            "  SELECT ID,\n" +
            "         militante_cedula,\n" +
            "         phone_number,\n" +
            "         whats_app,\n" +
            "         registered_by_id,\n" +
            "         ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "      s.is_se is not true " +
            "    AND\n" +
            "    CASE\n" +
            "      WHEN :parentId IS NULL THEN\n" +
            "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
            "      END \n" +
            "\tUNION ALL\n" +
            "  SELECT\n" +
            "    s.ID,\n" +
            "    s.militante_cedula,\n" +
            "    s.phone_number,\n" +
            "    s.whats_app,\n" +
            "    s.registered_by_id,\n" +
            "    r.PATH || s.ID AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
            "  WHERE\n" +
            "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
            ") SELECT count(1)\n" +
            "FROM\n" +
            "  simpatizantes_r A \n" +
            "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
            "  left JOIN simpatizantes s2 ON s2.ID = A.registered_by_id\n" +
            "      where  s1.is_se is not true")
    Integer countAll(@Param("parentId") Long parentId);

    @Query(nativeQuery = true, value = "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
            "  SELECT ID,\n" +
            "         militante_cedula,\n" +
            "         phone_number,\n" +
            "         whats_app,\n" +
            "         registered_by_id,\n" +
            "         ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "      s.is_se is not true " +
            "    AND\n" +
            "    CASE\n" +
            "      WHEN :parentId IS NULL THEN\n" +
            "        registered_by_id IS NULL ELSE registered_by_id = CAST(CAST(:parentId AS TEXT) AS BIGINT)\n" +
            "      END \n" +
            "\tUNION ALL\n" +
            "  SELECT\n" +
            "    s.ID,\n" +
            "    s.militante_cedula,\n" +
            "    s.phone_number,\n" +
            "    s.whats_app,\n" +
            "    s.registered_by_id,\n" +
            "    r.PATH || s.ID AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
            "  WHERE\n" +
            "      s.ID != ANY ( r.PATH ) and s.is_se is not true \n" +
            ") SELECT count(1)\n" +
            "FROM\n" +
            "  simpatizantes_r A \n" +
            "\tINNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
            "  left JOIN simpatizantes s2 ON s2.ID = A.registered_by_id\n" +
            "  left join users u on u.militante_cedula = s1.militante_cedula" +
            "      where  s1.is_se is not true and u.role in ('COORDINADOR','MULTIPLICADOR')")
    Integer countCoordinators(@Param("parentId") Long parentId);


    @Query(nativeQuery = true, value = "SELECT  " +
            "concat(p.nombres, ' ', p.apellido1,' ', p.apellido2) as \"nombre\", " +
            " concat(substring(a.militante_cedula from 0 for 4),'-',substring(a.militante_cedula from 4 for 7),'-',substring(a.militante_cedula from 11 for 1)) as \"cedula\",  " +
            " a.phone_number as \"telefono\",  " +
            " a.whats_app as \"whatsapp\", " +
            " u.role as \"rol\", " +
            " u.registered_count \"simpatizantes_registrados\", " +
            " concat(p2.nombres, ' ', p2.apellido1,' ', p2.apellido2) as \"coordinador\", " +
            "  concat(substring(s2.militante_cedula from 0 for 4),'-',substring(s2.militante_cedula from 4 for 7),'-',substring(s2.militante_cedula from 11 for 1)) as \"cedula_coordinador\" " +
            "FROM " +
            "  simpatizantes A " +
            " INNER JOIN simpatizantes s1 ON s1.ID = A.id " +
            "  INNER JOIN padron P ON P.cedula = s1.militante_cedula " +
            "  left JOIN simpatizantes s2 ON s2.ID = A.registered_by_id " +
            "  left JOIN padron p2 ON p2.cedula = s2.militante_cedula " +
            "  left JOIN users u ON u.militante_cedula = s1.militante_cedula " +
            " where s1.is_se is not true")
    Stream<SimpatizanteReportResponse> getReportData();


    @Query(
            nativeQuery = true,
            value = "select  p.provincia as \"provincia\",\n" +
                    "count(1) filter (WHERE date(created_date at time zone 'UTC' at time zone 'America/Santo_Domingo')\\:\\:text = :date) as \"cantidad\"\n" +
                    "from simpatizantes s join padron p on s.militante_cedula = p.cedula GROUP BY 1")
    Stream<ReporteProvincia> getReporteProvincia(@Param("date") String date);

    @Query(nativeQuery = true, value = "delete from simpatizantes where id in (\n" +
            "\n" +
            "WITH RECURSIVE simpatizantes_r ( ID, militante_cedula, phone_number, whats_app, registered_by_id, PATH ) AS (\n" +
            "  SELECT ID,\n" +
            "         militante_cedula,\n" +
            "         phone_number,\n" +
            "         whats_app,\n" +
            "         registered_by_id,\n" +
            "         ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "      militante_cedula = :cedula\n" +
            "\tUNION ALL\n" +
            "  SELECT\n" +
            "    s.ID,\n" +
            "    s.militante_cedula,\n" +
            "    s.phone_number,\n" +
            "    s.whats_app,\n" +
            "    s.registered_by_id,\n" +
            "    r.PATH || s.ID AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "      INNER JOIN simpatizantes_r r ON r.ID = s.registered_by_id\n" +
            "  WHERE\n" +
            "      s.ID != ANY ( r.PATH ) \n" +
            ") SELECT a.id\n" +
            "FROM\n" +
            "  simpatizantes_r A\t\n" +
            ")")
    @Modifying
    void deleteAllUnderSimpatizante(@Param("cedula") String cedula);
}
