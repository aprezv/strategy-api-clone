package com.strategy.api.base.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

/**
 * Created on 8/10/18.
 */
public interface BaseEnum {

    default String getDisplayName() {
        try {
            String dn = ResourceBundle
                    .getBundle(getMessageBundle(), LocaleContextHolder.getLocale())
                    .getString(this.getClass().getCanonicalName() + "." + name());
            return dn != null ? dn : getName();
        } catch (Exception e) {
            LoggerFactory
                    .getLogger(this.getClass())
                    .warn("No message ["
                            + this.getClass().getCanonicalName()
                            + "." + name()
                            + "] found in bundle ["
                            + getMessageBundle()
                            + "] with Locale ["
                            + LocaleContextHolder.getLocale() + "]"
                    );
        }
        return getName();
    }

    String name();

    @JsonIgnore
    String getName();

    @JsonIgnore
    String getMessageBundle();

    int ordinal();

    default int getValue() {
        return ordinal();
    }

    default Class getClazz(){
        return this.getClass();
    }
}
