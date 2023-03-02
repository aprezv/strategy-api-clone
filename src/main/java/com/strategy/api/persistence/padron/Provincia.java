package com.strategy.api.persistence.padron;

import lombok.Data;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 2020-03-05.
 */
@Data
@Entity
@Subselect("SELECT * FROM provincia where region is not null")
public class Provincia {
    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Integer id;

    @Column(name = "descripcion", updatable = false, insertable = false)
    private String descripcion;
}
