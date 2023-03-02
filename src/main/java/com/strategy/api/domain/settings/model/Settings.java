package com.strategy.api.domain.settings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 6/16/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    private Integer sympathizerGoal;
    private String supportPhone;
}
