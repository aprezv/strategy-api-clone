package com.strategy.api.reports.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created on 2020-05-18.
 */
@Data
@Builder
@AllArgsConstructor
public class ExportField {
    String fieldName;
    String displayName;
}
