package com.strategy.api.persistence.simpatizantes.repositories;

import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.reports.SimpatizanteReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * Created on 10/1/22.
 */
public interface TodosLosSimpatizantes extends JpaRepository<SimpatizanteEntity, Long> {

    @Query(nativeQuery = true, value = "WITH RECURSIVE simpatizantes_r (\n" +
            "                                ID,\n" +
            "                                militante_cedula,\n" +
            "                                phone_number,\n" +
            "                                whats_app,\n" +
            "                                registered_by_id,\n" +
            "                                PATH\n" +
            "  ) AS (\n" +
            "  SELECT\n" +
            "    ID,\n" +
            "    militante_cedula,\n" +
            "    phone_number,\n" +
            "    whats_app,\n" +
            "    registered_by_id,\n" +
            "    ARRAY [ ID ] AS PATH\n" +
            "  FROM\n" +
            "    simpatizantes s\n" +
            "  WHERE\n" +
            "    registered_by_id IS NULL\n" +
            "  UNION ALL\n" +
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
            "      s.ID != ANY (r.PATH)\n" +
            ")\n" +
            "SELECT\n" +
            "  concat(p.nombres, ' ', p.apellido1, ' ', p.apellido2) AS \"nombre\", concat(substring(a.militante_cedula FROM 0 FOR 4), '-', substring(a.militante_cedula FROM 4 FOR 7), '-',    substring(a.militante_cedula FROM 11 FOR 1)) AS \"cedula\", a.phone_number AS \"telefono\", a.whats_app AS \"whatsapp\",\n" +
            "  case when u.role is null then u1.role else u.role end AS \"role\",\n" +
            "  case when m.id is null then m2.id else m.id end  as \"id_movimiento\",\n" +
            "  case when m.name is null then m2.name else m.name end as \"nombre_movimiento\",\n" +
            "  case when u.registered_count is null then u1.registered_count else u.registered_count end as \"simpatizantes_registrados\", concat(p2.nombres, ' ', p2.apellido1, ' ', p2.apellido2) AS \"coordinador\", concat(substring(s2.militante_cedula FROM 0 FOR 4), '-', substring(s2.militante_cedula FROM 4 FOR 7), '-', substring(s2.militante_cedula FROM 11 FOR 1)) AS \"cedula_coordinador\", CASE WHEN p.pld IS NULL THEN\n" +
            "                                                                                                                                                                                                                                                                                                                                                                                                FALSE\n" +
            "                                                                                                                                                                                                                                                                                                                                                                                              ELSE\n" +
            "                                                                                                                                                                                                                                                                                                                                                                                                TRUE\n" +
            "    END AS pld, p.codigorecinto AS \"codigo_recinto\", p.idcolegio AS \"id_colegio\", CASE WHEN s1.is_se IS NULL THEN\n" +
            "                                                                                         'Strategy'\n" +
            "                                                                                       ELSE\n" +
            "                                                                                         'Sector Externo'\n" +
            "    END AS \"plataforma\"\n" +
            "FROM\n" +
            "  simpatizantes_r A\n" +
            "    INNER JOIN simpatizantes s1 ON s1.ID = A.id\n" +
            "    INNER JOIN padron P ON P.cedula = s1.militante_cedula\n" +
            "    LEFT JOIN simpatizantes s2 ON s2.ID = A.registered_by_id\n" +
            "    LEFT JOIN padron p2 ON p2.cedula = s2.militante_cedula\n" +
            "    LEFT JOIN users u ON u.militante_cedula = s1.militante_cedula\n" +
            "    left join se_users u1 on u1.militante_cedula = s1.militante_cedula\n" +
            "    left join se_users su on su.militante_cedula = s2.militante_cedula\n" +
            "    left join se_movimientos m on m.id = su.movimiento_id\n" +
            "    left join se_movimientos m2 on u1.movimiento_id = m2.id;")
     Stream<SimpatizanteReportResponse> getAll();
}
