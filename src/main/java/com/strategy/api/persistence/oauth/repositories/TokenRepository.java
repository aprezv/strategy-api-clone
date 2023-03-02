package com.strategy.api.persistence.oauth.repositories;

import com.strategy.api.persistence.oauth.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 9/5/22.
 */
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
}
