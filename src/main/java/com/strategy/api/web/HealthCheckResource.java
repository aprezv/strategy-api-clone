package com.strategy.api.web;

import com.strategy.api.reports.ReporteSimpatizantesExcel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2020-09-01.
 */
@RestController
@RequestMapping("/healthcheck")
public class HealthCheckResource {
    private final ReporteSimpatizantesExcel excel;

    public HealthCheckResource(ReporteSimpatizantesExcel excel) {
        this.excel = excel;
    }

    @Transactional(readOnly = true)
    @GetMapping
    ResponseEntity healthCheck(){
        excel.exportAndUpload();
        return ResponseEntity.ok().build();
    }
}
