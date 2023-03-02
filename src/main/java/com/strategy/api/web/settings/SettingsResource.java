package com.strategy.api.web.settings;

import com.strategy.api.domain.settings.SettingsRepository;
import com.strategy.api.domain.settings.model.Settings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 6/16/22.
 */
@RestController
@RequestMapping("/settings")
public class SettingsResource {
    private final SettingsRepository settingsRepository;

    public SettingsResource(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(settingsRepository.getSettings());
    }
}
