package com.strategy.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2021-01-07.
 */
@Data
@ConfigurationProperties(prefix = "application")
public class AppProperties {
    private String baseDomain;
}
