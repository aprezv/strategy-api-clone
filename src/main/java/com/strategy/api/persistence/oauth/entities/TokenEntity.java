package com.strategy.api.persistence.oauth.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created on 2021-03-31.
 */
@Entity
@Table(name = "tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TokenEntity {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT", name = "access_token")
    private String accessToken;

    @Column(columnDefinition = "TEXT", name = "refresh_token")
    private String refreshToken;

    @Column
    private String teamId;

    @Column
    private String enterpriseId;

    @Column
    private Long expiresIn;

    @Column
    @LastModifiedDate
    private Date lastModifiedDate;

    @Column
    private Boolean isEncrypted;

}
