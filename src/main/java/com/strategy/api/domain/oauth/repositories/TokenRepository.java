package com.strategy.api.domain.oauth.repositories;

import com.strategy.api.domain.oauth.model.Token;

/**
 * Created on 2021-03-31.
 */
public interface TokenRepository {
    Token saveToken(Token token);
    String findByWorkspaceExternalId(String workspaceId);

    void deleteByWorkspaceId(String externalId);
}
