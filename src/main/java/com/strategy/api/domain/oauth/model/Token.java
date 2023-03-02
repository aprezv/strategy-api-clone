package com.strategy.api.domain.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created on 2021-03-31.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String enterpriseId;
    private String teamId;
    private Date lastModifiedDate;
    private Boolean isEncrypted;
}
