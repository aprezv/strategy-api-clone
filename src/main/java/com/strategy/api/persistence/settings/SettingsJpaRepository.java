package com.strategy.api.persistence.settings;

import com.strategy.api.persistence.settings.entities.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 6/16/22.
 */
public interface SettingsJpaRepository extends JpaRepository<SettingsEntity, Long> {
    SettingsEntity findFirstByOrderById();
}
