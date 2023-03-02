package com.strategy.api.domain.simpatizantes;

import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Created on 6/13/22.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Simpatizante {
    private Long id;
    private Militante militante;
    private Simpatizante registeredBy;
    private String phoneNumber;
    private String whatsApp;
    private Long lat;
    private Long lng;
}
