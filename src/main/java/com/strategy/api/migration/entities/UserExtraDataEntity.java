package com.strategy.api.migration.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 6/20/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Subselect("select * from wp7i_users_extradata")
public class UserExtraDataEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "celular")
    private String celular;

    @Column(name = "whatsapp")
    private String whatsapp;

    @Column(name = "alias")
    private String alias;

    @Column(name = "telefonocasa")
    private String telefonoCasa;

    @Column(name = "email")
    private String email;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "profesionoficio")
    private String profesionoficio;

    @Column(name = "Cedula")
    private String cedula;

}

/*

    CREATE TABLE "public"."wp7i_users_extradata" (
        "user_id" int4 NOT NULL,
        "created_by" int4 NOT NULL,
        "nombre" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
        "apellido" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
        "cedula" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "alias" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "direccion" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "provincia" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "municipio" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "distritomunicipal" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "seccionparaje" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "celular" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "telefonocasa" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "recinto" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "profesionoficio" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "ispld" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "whatsapp" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "twitter" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "instagram" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "facebook" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "organization_member" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "organization" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "observaciones" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "movimiento" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "codigorecinto" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "codigocolegio" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "fechanacimiento" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "idsexo" varchar(10) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "codigocircunscripcion" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "paisvotacion" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "demarcacionvotacion" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "desc_ciudad" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "direccionrecinto" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
        "is_exterior" int4 NOT NULL DEFAULT 0
        )
        ;
        ALTER TABLE "public"."wp7i_users_extradata" OWNER TO "master_user";

        -- ----------------------------*/
