package com.strategy.api.web.register.dto;

import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 6/13/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String cedula;
    private String phone;
    private String whatsApp;
    private Long userId;
    private SimpatizanteEntity registeredById;
}
