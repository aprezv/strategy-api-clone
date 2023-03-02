package com.strategy.api.migration.entities;

import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 6/20/22.
 */
@Entity
@Subselect("select * from wp7i_users")
public class WordpressUserEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_login")
    private String userLogin;

}
