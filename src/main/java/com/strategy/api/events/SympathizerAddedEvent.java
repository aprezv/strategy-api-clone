package com.strategy.api.events;

import com.strategy.api.domain.simpatizantes.Simpatizante;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * Created on 9/5/22.
 */
@Data
public class SympathizerAddedEvent extends ApplicationEvent {
    private String cedula;

    @Builder
    public SympathizerAddedEvent(String cedula, Object source) {
        super(source);
        this.cedula = cedula;
    }
}
