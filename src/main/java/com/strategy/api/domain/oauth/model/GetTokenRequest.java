package com.strategy.api.domain.oauth.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created on 2021-03-31.
 */
@Data
@Builder
public class GetTokenRequest {
    private Long entityId;
}
