package com.strategy.api.reports.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created on 2020-03-14.
 */
@Data
@AllArgsConstructor
public class ReportFilter {
    private String filter;
    private String value;
}
