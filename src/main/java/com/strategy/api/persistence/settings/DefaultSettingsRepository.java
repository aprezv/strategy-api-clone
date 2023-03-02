package com.strategy.api.persistence.settings;

import com.strategy.api.domain.settings.SettingsRepository;
import com.strategy.api.domain.settings.model.Settings;
import com.strategy.api.persistence.settings.entities.SettingsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

/**
 * Created on 6/16/22.
 */
@Repository
public class DefaultSettingsRepository implements SettingsRepository {
    private final SettingsJpaRepository jpaRepo;
    private final ModelMapper modelMapper;

    public DefaultSettingsRepository(SettingsJpaRepository jpaRepo, ModelMapper modelMapper) {
        this.jpaRepo = jpaRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public Settings getSettings() {
        SettingsEntity entity = jpaRepo.findFirstByOrderById();
        return modelMapper.map(entity, Settings.class);
    }
}
