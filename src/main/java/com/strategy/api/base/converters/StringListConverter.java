package com.strategy.api.base.converters;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2020-11-06.
 */
public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if(CollectionUtils.isEmpty(list))
            return "";

        return String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if(StringUtils.isEmpty(dbData))
            return new ArrayList<>();

        return Arrays.stream(dbData.split(","))
                .collect(Collectors.toList());
    }

}
