package com.strategy.api.domain.oauth.services;

import com.strategy.api.domain.oauth.model.GetTokenRequest;
import com.strategy.api.domain.oauth.model.Token;

/**
 * Created on 2021-03-31.
 */
public interface TokenService {
    Token saveToken(Token token);
    Token saveSlackToken();
    Token getToken(GetTokenRequest request);

}
