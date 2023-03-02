package com.strategy.api.persistence.settings.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created on 6/16/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "settings")
public class SettingsEntity {
    @Id
    @SequenceGenerator(name = "settings_id_pk_sequence", sequenceName = "settings_id_pk_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settings_id_pk_sequence")
    @Column(name = "id")
    private Long id;

    private Integer sympathizerGoal;

    private String supportPhone;
}
