package com.strategy.api.reports;

import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 8/3/22.
 */
@Component
public class SimpatizantesPorProvinciaPorDiaExcel {
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;

    public SimpatizantesPorProvinciaPorDiaExcel(final SimpatizanteJpaRepository simpatizanteJpaRepository) {
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
    }

    @Transactional
    public Resource export(String date) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            createHeaders(sheet, date);
            AtomicInteger index = new AtomicInteger(1);
            simpatizanteJpaRepository.getReporteProvincia(date)
                    .forEach((reportResponse) -> fillRow(sheet, reportResponse, index.getAndIncrement()));
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            workbook.write(bao);
            bao.flush();
            bao.close();
            return new ByteArrayResource(bao.toByteArray());

        } catch (IOException ignore) {
            throw new RuntimeException();
        }
    }


    private void createHeaders(final XSSFSheet sheet, String date) {
        String dateString = date;

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Provincia");
        header.createCell(1).setCellValue(dateString);

    }

    private void fillRow(final XSSFSheet sheet, final ReporteProvincia dbRow, final int index) {

        XSSFRow row = sheet.createRow(index );

        row.createCell(0).setCellValue(dbRow.getProvincia());
        row.createCell(1).setCellValue(dbRow.getCantidad());
    }

}
