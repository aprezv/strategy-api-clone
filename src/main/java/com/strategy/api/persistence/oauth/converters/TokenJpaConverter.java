package com.strategy.api.persistence.oauth.converters;

import com.strategy.api.base.converters.JpaConverter;
import com.strategy.api.domain.oauth.model.Token;
import com.strategy.api.persistence.oauth.entities.TokenEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created on 2021-03-31.
 */
@Component
public class TokenJpaConverter implements JpaConverter<Token, TokenEntity> {
    @Override
    public Token fromJpaEntity(TokenEntity tokenEntity) {
        return Optional.ofNullable(tokenEntity).map(t ->
                Token.builder()
                        .id(t.getId())
                        .accessToken(t.getAccessToken())
                        .refreshToken(t.getRefreshToken())
                        .expiresIn(t.getExpiresIn())
                        .enterpriseId(t.getEnterpriseId())
                        .teamId(t.getTeamId())
                        .lastModifiedDate(t.getLastModifiedDate())
                        .isEncrypted(t.getIsEncrypted())
                        .build())
                .orElse(null);
    }

    @Override
    public TokenEntity toJpaEntity(Token token1) {
        return Optional.ofNullable(token1)
                .map( token -> TokenEntity.builder()
                        .id(token.getId())
                        .accessToken(token.getAccessToken())
                        .expiresIn(token.getExpiresIn())
                        .enterpriseId(token.getEnterpriseId())
                        .teamId(token.getTeamId())
                        .lastModifiedDate(token.getLastModifiedDate())
                        .isEncrypted(token.getIsEncrypted())
                        .refreshToken(token.getRefreshToken()).build())
                .orElse(null);
    }
}
