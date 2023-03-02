package com.strategy.api.web.padroncillo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 9/2/22.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneratePadroncilloRequest {
    private Long idSimpatizante;
    private Boolean byCirc;
}
