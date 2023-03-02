package com.strategy.api.base.converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.strategy.api.base.model.jpa.BaseEnum;

import java.util.HashMap;
import java.util.Map;

public class BaseEnumConverter extends StdConverter<BaseEnum, Map> {

    @Override
    public Map convert(BaseEnum baseEnum) {
        return new HashMap(){{
            put("name", baseEnum.name());
            put("displayName", baseEnum.getDisplayName());
            put("value", baseEnum.getValue());
        }};
    }
}
