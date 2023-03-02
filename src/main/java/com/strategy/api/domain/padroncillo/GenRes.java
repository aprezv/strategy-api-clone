package com.strategy.api.domain.padroncillo;

import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.persistence.respositories.PadroncilloResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

/**
 * Created on 9/29/22.
 */
@Data
@Builder
public class GenRes {
    PadroncilloResponse simpatizante;
    Resource resource;
    Integer count;
}
