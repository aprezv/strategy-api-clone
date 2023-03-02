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
@Subselect("select * from wp7i_usermeta")
public class UserMetaDataEntity {
    @Id
    @Column(name = "umeta_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "meta_key")
    private String metaKey;

    @Column(name = "meta_value")
    private String metaValue;

}
