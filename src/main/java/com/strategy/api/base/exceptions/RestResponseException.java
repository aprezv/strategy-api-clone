package com.strategy.api.base.exceptions;


import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created on 6/14/18.
 */
public abstract class RestResponseException extends RuntimeException {
    public Object[] getParams(){
        return new Object[]{};
    }
    private String path;
    public abstract int getStatus();
    //public abstract String getMessage();
    public Date getTimestamp(){
        return new Date();
    }
    //public abstract String getCode();
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        try {
            String dn = ResourceBundle
                    .getBundle("messages.Exceptions", LocaleContextHolder.getLocale())
                    .getString(String.format("%s.message", getClass().getName()));
            return dn != null ? dn : "";
        } catch (Exception e) {
            LoggerFactory
                    .getLogger(this.getClass())
                    .warn("No message ["
                            + this.getClass().getCanonicalName()
                            + "] found in bundle ["
                            +" Exceptions "
                            + "] with Locale ["
                            + LocaleContextHolder.getLocale() + "]"
                    );
        }
        return "";
    }


}
