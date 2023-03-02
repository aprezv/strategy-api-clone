package com.strategy.api.persistence.respositories;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strategy.api.base.converters.BaseEnumConverter;
import com.strategy.api.base.model.jpa.BaseEnum;

/**
 * Created on 7/8/22.
 */
@JsonSerialize(converter = BaseEnumConverter.class)
public enum CallResponse implements BaseEnum {
    ABEL_MARTINEZ("Abel Martínez"),
    MARGARITA_CEDENO("Margarita Cedeño"),
    FRANCISCO_DOMINGUEZ_BRITO("Dominguez Brito"),
    MARITZA_HERNANDEZ("Maritza Hernandez"),
    KAREN_RICARDO("Karen Ricardo"),
    LUIS_DE_LEON ("Luis de León"),
    OTRO_PARTIDO ("Otro Partido"),
    NO_VOTARA("No Votará"),
    INDECISO ("Indeciso");

    String text;

    CallResponse(String text) {
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
