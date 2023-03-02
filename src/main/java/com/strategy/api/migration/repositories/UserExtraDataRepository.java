package com.strategy.api.migration.repositories;

import com.strategy.api.migration.entities.UserExtraDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 6/20/22.
 */
public interface UserExtraDataRepository extends JpaRepository<UserExtraDataEntity, Long> {
}
