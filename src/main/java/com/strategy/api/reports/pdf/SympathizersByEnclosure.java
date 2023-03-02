/*
package com.strategy.api.reports.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.UnitValue;
import io.heartbits.sympathizers.entities.Sympathizer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.List;

*/
/**
 * Created on 2020-03-12.
 *//*

public class SympathizersByEnclosure {

    public Resource generateReport(List<Sympathizer> sympathizers, List<String> filters) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));

        Document document = new Document(pdfDocument,PageSize.A4);
        document.setMargins(10f,10f,10f,10f);

        document.add(CustomElements.getHeader());
        document.add(CustomElements.getSubHeader("Reporte por mesa", List.of(new ReportFilter("Mesa", "0550"), new ReportFilter("Recinto", "Pueblo Nuevo"))));

        if(!CollectionUtils.isEmpty(sympathizers)){
            //document.add(getSympathizerTable(sympathizers));

            document.add(getSympathizerBoxes(sympathizers));

        }


        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }


    private IBlockElement getSympathizerTable(List<Sympathizer> sympathizers){
        Table table = new Table(5);

        table.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        sympathizers.forEach((sympathizer)->this.addSympathizerRow(table, sympathizer));

        return table;

    }


    private void addSympathizerRow( Table table, Sympathizer sympathizer){

        table.addCell(getText(String.format("%s %s", sympathizer.getNombres(), sympathizer.getApellidos())));
        table.addCell(getText(sympathizer.getCedula()));
        table.addCell(getText(sympathizer.getPhoneNumber()));
        table.addCell(getText(sympathizer.getRecintoElectoral()));
        table.addCell(getText(sympathizer.getRegisteredBy().getName()));
    }


    private Table getSympathizerBoxes(List<Sympathizer> sympathizers){

        Table table = new Table(2);
        table.setWidth(new UnitValue(UnitValue.PERCENT, 100));

        int count = 1;
        sympathizers.forEach((sympathizer -> addSympathizerBox(table, sympathizer, count)));

        return table;
    }

    private Table addSympathizerBox(Table table, Sympathizer sympathizer, int count){

        final String DEMO_IMG = "classpath:images/demostenes_oficial.jpg";
        Table mainTable = new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 10),new UnitValue(UnitValue.PERCENT, 90)});
        mainTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        mainTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
        Table imageTable = new Table(1);
        imageTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
        Table detailTable = new Table(2);
        detailTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        detailTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);

        detailTable.addCell(
                new Cell(1,3)
                        .add(getText(String.format("%s %s", sympathizer.getNombres(), sympathizer.getApellidos())).setBold())
                        .setBorderTop(Border.NO_BORDER)
        );

        detailTable.addCell(new Cell().add(getText(sympathizer.getCedula())));
        detailTable.addCell(new Cell().add(getText(String.format("Tel: %s", sympathizer.getPhoneNumber()))));

        detailTable.startNewRow();

        detailTable.addCell(
                new Cell(1,2)
                        .add(getText(sympathizer.getRecintoElectoral()))
                        .setBorderBottom(Border.NO_BORDER)
        );
        //detailTable.addCell(getText(sympathizer.getColegioElectoral()));

        try {
            imageTable.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .add(new Image(ImageDataFactory.create(DEMO_IMG)).setHeight(54f).setWidth(54f))
                            .setPadding(0)
                            .setMargin(0)
            );

            mainTable.addCell(
                    new Cell().add(imageTable).setPadding(0).setBorderRight(Border.NO_BORDER)
            );
            mainTable.addCell(
                    new Cell().add(detailTable)
                            .setMargin(0)
                            .setPadding(0)
                            .setBorderLeft(Border.NO_BORDER)
                            .setBorderRight(Border.NO_BORDER)
            );



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        table.addCell(new Cell().add(mainTable).setBorder(Border.NO_BORDER));
        if(count % 2 == 0){
            mainTable.startNewRow();
        }
        return mainTable;
    }

    private Paragraph getText(String text){
        return new Paragraph(new Text(text).setFontSize(9));
    }
}
*/
