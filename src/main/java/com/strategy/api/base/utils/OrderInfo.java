package com.strategy.api.base.utils;

import org.springframework.data.domain.Sort;

/**
 *
 */
public class OrderInfo {
    private String field;
    private Sort.Direction direction;

    public OrderInfo(String field) {
        this(field, Sort.Direction.ASC);
    }

    public OrderInfo(String field, Sort.Direction direction) {
        setField(field);
        setDirection(direction);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
}
