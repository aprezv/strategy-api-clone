package com.strategy.api.web.reports;

import com.strategy.api.reports.ReporteSimpatizantesExcel;
import com.strategy.api.reports.SimpatizantesPorProvinciaPorDiaExcel;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created on 8/3/22.
 */
@RestController
@RequestMapping("/reports/excel")
public class ReporteSimpatizantesExcelResource {
    public ReporteSimpatizantesExcelResource(ReporteSimpatizantesExcel reporteSimpatizantesExcel, SimpatizantesPorProvinciaPorDiaExcel porProvinciaPorDiaExcel) {
        this.reporteSimpatizantesExcel = reporteSimpatizantesExcel;
        this.porProvinciaPorDiaExcel = porProvinciaPorDiaExcel;
    }

    private final ReporteSimpatizantesExcel reporteSimpatizantesExcel;
    private final SimpatizantesPorProvinciaPorDiaExcel porProvinciaPorDiaExcel;

    @GetMapping
    public ResponseEntity<Resource> getReport() throws IOException {

        Resource resource = reporteSimpatizantesExcel.export();
        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(resource);
    }

    @GetMapping("/provincia")
    public ResponseEntity<Resource> reporteProvinciaPorDia(@RequestParam("date") String fecha) throws IOException  {
        Resource resource = porProvinciaPorDiaExcel.export(fecha);
        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(resource);
    }
}
