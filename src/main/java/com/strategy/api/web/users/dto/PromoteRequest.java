package com.strategy.api.web.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 6/14/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoteRequest {
    private String cedula;
    private Boolean allowCreateCoordinators;
    private Boolean isSimpatizante;
    private String phone;
    private String whatsApp;
}
