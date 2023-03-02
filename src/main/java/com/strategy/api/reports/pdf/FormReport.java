/*
package com.strategy.api.reports.pdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import io.heartbits.sympathizers.entities.Block;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;

*/
/**
 * Created on 2020-03-12.
 *//*

public class FormReport {

    public Resource generateReport(Block block, Integer qty) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));

        Document document = new Document(pdfDocument,PageSize.A4.rotate());
        document.setMargins(10f,10f,10f,10f);

        for(int i = 1; i <= qty ; i++){
            document.add(CustomElements.getHeader());
            document.add(getSubHeader(block, i));
            document.add(getRows());
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }

        CustomElements.applyWatermark(pdfDocument, PageSize.A4.rotate());

        pdfDocument.removePage(pdfDocument.getLastPage());

        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }


    private Table getRows(){
      Table table = new Table(new UnitValue[]{
              UnitValue.createPercentValue(1),
              UnitValue.createPercentValue(27),
              UnitValue.createPercentValue(18),
              UnitValue.createPercentValue(18),
              UnitValue.createPercentValue(36),
      });
      table.setWidth(UnitValue.createPercentValue(100));

      table.addCell(getRowNumber("NO."));
      table.addCell(getHeaderCell("NOMBRE"));
      table.addCell(getHeaderCell("CEDULA"));
      table.addCell(getHeaderCell("CELULAR"));
      table.addCell(getHeaderCell("DIRECCION"));

      for(int i = 1; i < 21 ; i++ ){
          table.addCell
                  (new Cell().add(
                          new Paragraph(
                                  new Text(Integer.toString(i))
                          )
                                  .setPaddingLeft(0)
                                  .setPaddingRight(0)
                                  .setMarginLeft(0)
                                  .setMarginRight(0)
                                  .setFontSize(10))
                          .setWidth(1)
                          .setTextAlignment(TextAlignment.CENTER)
                          .setPaddingLeft(0)
                          .setPaddingRight(0)
                          .setMarginLeft(0)
                          .setMarginRight(0)
                          .setWidth(1)
                  );

          for(int j = 0; j < 4 ; j++){
              table.addCell(new Cell());
          }
      }

      table.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
      return table;
    }

    private Table getSubHeader(Block block, int number){
        Table containerTable = new Table(new UnitValue[]{UnitValue.createPercentValue(50), UnitValue.createPercentValue(50)});
        containerTable.setWidth(UnitValue.createPercentValue(100));
        containerTable.setFontSize(9);
        containerTable.setVerticalAlignment(VerticalAlignment.BOTTOM);

        Table blockTable = new Table(1);
        blockTable.setWidth(UnitValue.createPercentValue(100));
        blockTable.addCell(
                new Cell().add(
                new Paragraph(
                        new Text("NO.:")
                .setBold()) )
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
       );
        blockTable.startNewRow();
        blockTable.addCell(
                new Cell().add(
                new Paragraph().add(new Text("BLOQUE: ").setBold()))
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );

        blockTable.addCell(
                new Cell().add(
                        new Paragraph().add(new Text("COORDINADOR: ").setBold()))
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );


        Table registrantTable = new Table(new UnitValue[]{UnitValue.createPercentValue(50), UnitValue.createPercentValue(50)});
        registrantTable.setWidth(UnitValue.createPercentValue(100));
        registrantTable.addCell(
                new Cell(1,2)
                        .add(new Paragraph("ENCARGADO: ")).setBold()
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );
        registrantTable.startNewRow();
        registrantTable.addCell(
                new Cell()
                        .add(new Paragraph("CEDULA: ")).setBold()
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );
        registrantTable.addCell(
                new Cell()
                        .add(new Paragraph("TELEFONO: ")).setBold()
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );

        registrantTable.addCell(
                new Cell(1,2)
                        .add(new Paragraph("DIRECCION: ")).setBold()
                        .setBorderLeft(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderTop(Border.NO_BORDER)
        );

        containerTable.addCell(
                new Cell(1,2)
                        .add(new Paragraph(new Text("FORMULARIO DE SIMPATIZANTES")))
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(12)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
        );

        containerTable.addCell(new Cell().add(blockTable).setBorderRight(Border.NO_BORDER));
        containerTable.addCell(registrantTable);
        containerTable.setMarginBottom(10);

        return containerTable;
    }

    private Cell getHeaderCell(String header){
        return new Cell().setBackgroundColor(new DeviceRgb(2,172,90))
                .add(new Paragraph(new Text(header)))
                .setFontColor(ColorConstants.WHITE)
                .setFontSize(8)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell getRowNumber(String header){
        return new Cell().setBackgroundColor(new DeviceRgb(2,172,90))
                .add(new Paragraph(new Text(header)).setPaddingLeft(0)
                        .setPaddingRight(0)
                        .setMarginLeft(0)
                        .setMarginRight(0))
                .setFontColor(ColorConstants.WHITE)
                .setFontSize(8)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
    }


}
*/
