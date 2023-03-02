package com.strategy.api.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created on 2020-11-19.
 */
@Data
@Builder
public class ResultsPage<T> {
    private List<T> content;
    private Integer number;
    private Integer totalPages;
    private Long totalElements;
    private Integer numberOfElements;
    private Boolean first;
    private Boolean last;
}
