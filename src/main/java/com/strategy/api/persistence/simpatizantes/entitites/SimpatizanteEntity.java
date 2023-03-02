package com.strategy.api.persistence.simpatizantes.entitites;

import com.strategy.api.base.model.jpa.BaseEntity;
import com.strategy.api.persistence.padron.MilitanteEntity;
import com.strategy.api.persistence.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created on 6/13/22.
 */
@Data
@Entity
@Table(name = "simpatizantes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SimpatizanteEntity {

    @Id
    @SequenceGenerator(name = "simpatizante_id_pk_sequence", sequenceName = "simpatizante_id_pk_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "simpatizante_id_pk_sequence")
    @Column(name = "id")
    private Long id;

    @OneToOne
    private MilitanteEntity militante;

    @ManyToOne
    private SimpatizanteEntity registeredBy;

    @Column
    private String phoneNumber;

    @Column
    private String whatsApp;

    @Column
    private String email;

    @Column
    private String twitter;

    @Column
    private String instagram;

    @Column
    private String facebook;

    @Column
    private String profesion;

    @Column
    private String telefonoCasa;

    @Column
    private String alias;

    @Column
    private Boolean migrated;

    @Column(name = "is_se")
    private Boolean isSe;

    @LastModifiedDate
    private Date lastModifiedDate;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @Column
    private Boolean votoCoordinador;

    @Column
    private Boolean votoCentro;

    @Column
    private Boolean verificadoEsquina;

    @Column(updatable = false)
    private Long lat;

    @Column(updatable = false)
    private Long lng;

}
