package com.strategy.api.base.utils;

import com.strategy.api.base.exceptions.BadRequestException;
import com.strategy.api.base.exceptions.InvalidFieldException;
import com.strategy.api.base.model.http.EditFieldRequest;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created on 2020-08-11.
 */
public class FieldUtils {

    private final Class aClass;
    private final String entityName;

    public FieldUtils(Class aClass, String entityName) {
        this.aClass = aClass;
        this.entityName = entityName;
    }

    public  void updateField( Object object, EditFieldRequest request) {
        Field field = getClassField(request.getFieldName(), aClass);
        field.setAccessible(true);

        try {

            if(field.getType().isEnum()){
                field.set(object, Enum.valueOf((Class<Enum>) field.getType(), request.getValue().toString()));
            } else if(field.getType() == Date.class){
                Date date = request.getValue() != null
                        ? DatatypeConverter.parseDateTime(request.getValue().toString()).getTime()
                        : null;
                field.set(object, date);
            } else {
                field.set(object, request.getValue());
            }

        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new BadRequestException("");
        }
    }

    private Field getClassField(final String fieldName, final Class fieldClass){


        try {
            return fieldClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e){
            if(fieldClass.getSuperclass() != null)
                return getClassField(fieldName, fieldClass.getSuperclass());

            throw new InvalidFieldException(entityName, fieldName);
        }

    }
}
