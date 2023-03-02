package com.strategy.api.util;

/**
 * Created on 4/7/22.
 */
public class Percentage {
    public static long get(int count, int total) {
        return Math.round(Integer.valueOf(count).doubleValue() / Integer.valueOf(total).doubleValue() * 100);
    }

    public static long get(Long count, Long total) {
        return get(count.intValue(), total.intValue());
    }
}
