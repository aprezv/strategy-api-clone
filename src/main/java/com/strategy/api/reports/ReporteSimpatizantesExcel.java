package com.strategy.api.reports;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.strategy.api.fileuploader.models.FileUploadRequest;
import com.strategy.api.fileuploader.services.FileServiceS3;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import com.strategy.api.persistence.simpatizantes.repositories.TodosLosSimpatizantes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 8/3/22.
 */
@Slf4j
@Component
public class ReporteSimpatizantesExcel {
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;
    private final TodosLosSimpatizantes todosLosSimpatizantes;
    private final String fileName = "Simpatizantes.csv";
    private final String bucketName = "strategy-public";
    private final FileServiceS3 fileServiceS3;

    public ReporteSimpatizantesExcel(final SimpatizanteJpaRepository simpatizanteJpaRepository,
                                     final TodosLosSimpatizantes todosLosSimpatizantes,
                                     final FileServiceS3 fileServiceS3) {
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
        this.todosLosSimpatizantes = todosLosSimpatizantes;
        this.fileServiceS3 = fileServiceS3;
    }

    @Transactional
    public Resource export() {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            createHeaders(sheet);
            AtomicInteger index = new AtomicInteger(1);
            simpatizanteJpaRepository.getReportData()
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

    //@Scheduled(fixedDelay = 3600000, initialDelay = 60000)
    @Transactional
    public void exportAndUpload() {
        long start = System.currentTimeMillis();
        log.info("Generando archivo csv de simpatizantes");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("Nombre,Cédula,Telefono,WhatsApp,Rol,Simpatizantes Registrados,Coordidador,Cédula Coordinador,pld,Codigo Recinto,ID Colegio,Plataforma,ID Movimiento,Nombre Movimiento\n");
            todosLosSimpatizantes.getAll()
                    .forEach((reportResponse) -> {
                        try {
                            writer.write(getRow(reportResponse));
                        } catch (IOException e) {
                            log.error("Error generando linea ");
                        }
                    });

            writer.close();
            log.info("Archivo generado exitosamente");

            upload();
        } catch (Exception e) {
            log.error("Error generando csv de simpatizantes", e);
        }

        log.info("Archivo generado y subido correctamente. Proceso tomó {} minutos", (System.currentTimeMillis() - start)/60000);
    }

    private String getRow(SimpatizanteReportResponse dbRow) {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                StringEscapeUtils.escapeCsv(dbRow.getNombre()),
                StringEscapeUtils.escapeCsv(dbRow.getCedula()),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getTelefono()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getWhatsApp()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getRole()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getSimpatizantes_registrados()).map(String::valueOf).orElse("0")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getCoordinador()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getCedula_coordinador()).orElse("")),
                dbRow.getPld(),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getCodigo_recinto()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getId_colegio()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getPlataforma()).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getId_movimiento()).map(String::valueOf).orElse("")),
                StringEscapeUtils.escapeCsv(Optional.ofNullable(dbRow.getNombre_movimiento()).orElse(""))
                );
    }


    private void upload() {
        try {

            log.info("Subiendo archivo csv a s3");
            File file = new File(fileName);

            InputStream in = new FileInputStream(file);

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(in.available());
            meta.setContentType("text/csv");

            fileServiceS3.uploadFile(
                    FileUploadRequest.builder()
                            .key(fileName)
                            .publicAccess(true)
                            .contentLength(file.length())
                            .inputStream(in)
                            .contentType("text/csv")
                            .fileName(fileName)
                            .build(),
                    bucketName
            );

        }  catch ( SdkClientException | IOException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            log.error("Error generando csv de simpatizantes {} ", e.getMessage());
            log.error("Exception",  e);
        }
    }


    private void createHeaders(final XSSFSheet sheet) {

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nombre");
        header.createCell(1).setCellValue("Cédula");
        header.createCell(2).setCellValue("Teléfono");
        header.createCell(3).setCellValue("WhatsApp");
        header.createCell(4).setCellValue("Rol");
        header.createCell(5).setCellValue("Simpatizantes Registrados");
        header.createCell(6).setCellValue("Coordinador");
        header.createCell(7).setCellValue("Cédula Coordinador");

    }

    private void fillRow(final XSSFSheet sheet, final SimpatizanteReportResponse dbRow, final int index) {

        XSSFRow row = sheet.createRow(index);

        row.createCell(0).setCellValue(dbRow.getNombre());
        row.createCell(1).setCellValue(dbRow.getCedula());
        row.createCell(2).setCellValue(dbRow.getTelefono());
        row.createCell(3).setCellValue(dbRow.getWhatsApp());
        row.createCell(4).setCellValue(dbRow.getRole());
        row.createCell(5).setCellValue(Optional.ofNullable(dbRow.getSimpatizantes_registrados()).orElse(0));
        row.createCell(6).setCellValue(dbRow.getCoordinador());
        row.createCell(7).setCellValue(dbRow.getCedula_coordinador());
        row.createCell(7).setCellValue(dbRow.getCedula_coordinador());
    }

}
