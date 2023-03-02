package com.strategy.api.base.utils;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 11/5/2017.
 */
public class SortUtils {
    public static Sort processSort(String fields, String[] allowedSortFields) {
        if (fields == null || "".equals(fields.trim())) {
            throw new IllegalArgumentException("At least a field should be provided to sort on.");
        }

        List<Sort.Order> orders = new ArrayList<>();
        String[] fieldArr = fields.split(",");
        for (String field : fieldArr) {
            OrderInfo orderInfo = processOrder(field, allowedSortFields);
            orders.add(new Sort.Order(orderInfo.getDirection(), orderInfo.getField().trim()));
        }

        return Sort.by(orders);
    }

    public static OrderInfo processOrder(String field, String[] allowedFields) {
        if (field == null || "".equals(field.trim())) {
            throw new IllegalArgumentException("Invalid field to sort on.");
        }

        if (allowedFields == null || allowedFields.length == 0) {
            throw new IllegalArgumentException("An array of allowed field must be provided.");
        }

        if (field.charAt(0) != '+' && field.charAt(0) != '-') {
            field = "+" + field;
        }

        char sign = field.charAt(0);
        String property = field.substring(1);
        if (Arrays.asList(allowedFields).contains(property.trim())) {
            return new OrderInfo(property, sign == '+' ? Sort.Direction.ASC : Sort.Direction.DESC);
        } else {
            throw new IllegalArgumentException("Invalid field to sort on.");
        }
    }
}
