package com.strategy.api.persistence.respositories;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strategy.api.base.converters.BaseEnumConverter;
import com.strategy.api.base.model.jpa.BaseEnum;

/**
 * Created on 7/8/22.
 */
@JsonSerialize(converter = BaseEnumConverter.class)
public enum CallStatus implements BaseEnum {

    NUEVO("Nuevo"),
    NO_CONTESTADO("No Contestado"),
    OCUPADO("Ocupado"),
    SIN_SERVICIO("Sin Servicio"),
    CONTESTADO_WHATSAPP("Sin Servicio"),
    EQUIVOCADO("Equivocado"),
    CONTESTADO("Contestado");

    String text;

    CallStatus(String text) {
        this.text = text;
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getMessageBundle() {
        return "messages.Enums";
    }

    public String getText() {
        return text;
    }
}
