package com.strategy.api.base;

import lombok.Builder;
import lombok.Data;

/**
 * Created on 2020-11-19.
 */
@Data
@Builder
public class PageRequest {
    private int page;
    private int size;

    public static PageRequest of(final int page, final int size){
        return PageRequest.builder()
                .page(page)
                .size(size)
                .build();
    }
}
