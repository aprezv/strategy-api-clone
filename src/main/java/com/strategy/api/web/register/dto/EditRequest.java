package com.strategy.api.web.register.dto;

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
public class EditRequest {
    private Long id;
    private String phone;
    private String whatsApp;
}
