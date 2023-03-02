package com.strategy.api.domain.settings;

import com.strategy.api.domain.settings.model.Settings;

/**
 * Created on 6/16/22.
 */
public interface SettingsRepository {
    Settings getSettings();
}
