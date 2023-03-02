/*
package com.strategy.api.reports.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.UnitValue;
import io.heartbits.sympathizers.entities.Registrant;
import io.heartbits.sympathizers.entities.Sympathizer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

*/
/**
 * Created on 2020-03-12.
 *//*

public class SympathizersByRegistrant {

    public Resource generateReport(Map<Registrant, List<Sympathizer>> sympathizersMap, List<String> filters) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));

        Document document = new Document(pdfDocument,PageSize.A4.rotate());
        document.setMargins(10f,10f,10f,10f);

        sympathizersMap.entrySet().forEach((registrantListEntry -> {
            document.add(CustomElements.getHeader());
            document.add(getHeader("SIMPATIZANTES POR ENCARGADO",registrantListEntry.getKey()));
            if(!CollectionUtils.isEmpty(registrantListEntry.getValue())){
                document.add(getSympathizerBoxes(registrantListEntry.getValue()));
            }
            document.add(new AreaBreak());
        }));


        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }


    private Table getSympathizerBoxes(List<Sympathizer> sympathizers){

        Table table = new Table(new UnitValue[]{
                UnitValue.createPercentValue(23),
                UnitValue.createPercentValue(6),
                UnitValue.createPercentValue(6),
                UnitValue.createPercentValue(22),
                UnitValue.createPercentValue(3),
        });

        table.addCell(getTableHeader("NOMBRE"));
        table.addCell(getTableHeader("CÉDULA"));
        table.addCell(getTableHeader("TELÉFONO"));
        table.addCell(getTableHeader("RECINTO"));
        table.addCell(getTableHeader("MESA"));

        table.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        sympathizers.forEach((sympathizer -> addSympathizerRow(table, sympathizer)));

        return table;
    }

    private void addSympathizerRow(Table table, Sympathizer sympathizer){

        table.addCell(getText(String.format("%s %s", sympathizer.getNombres(), sympathizer.getApellidos())));
        table.addCell(getText(sympathizer.getCedula()));
        table.addCell(getText(sympathizer.getPhoneNumber()));
        table.addCell(getText(sympathizer.getRecinto() != null ? sympathizer.getRecinto().getDescripcion() : ""));
        table.addCell(getText(sympathizer.getColegio() != null ? sympathizer.getColegio().getCodigoReferencia() : ""));

    }


    public static Table getHeader(String reportName, Registrant registrant) {

        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell(new Cell(1, 2).add(CustomElements.getFilterCell(new ReportFilter("REPORTE", reportName))));
        table.startNewRow();


        table.addCell(CustomElements.getFilterCell(new ReportFilter("CIRCUNSCRIPCION", registrant.getBlock().getCircunscripcion().getDescripcion())));
        table.addCell(CustomElements.getFilterCell(new ReportFilter("BLOQUE", registrant.getBlock().getDescription())));
        table.addCell(CustomElements.getFilterCell(new ReportFilter("ENCARGADO", registrant.getName())));
        table.addCell(CustomElements.getFilterCell(new ReportFilter("TELEFONO", registrant.getPhoneNumber())));


        table.setMarginBottom(8);

        return table;
    }

    private Paragraph getText(String text){
        if(text==null)
            text = "";
        return new Paragraph(new Text(text).setFontSize(9));
    }

    private Paragraph getTableHeader(String text){
        if(text==null)
            text = "";
        return new Paragraph(new Text(text).setFontSize(9).setBold());
    }
}
*/
