package com.strategy.api.web.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 6/23/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReassignRequest {
    private Long sympathizerId;
    private Long coordinatorUserId;
}
