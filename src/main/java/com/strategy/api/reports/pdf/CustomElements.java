package com.strategy.api.reports.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created on 2020-02-03.
 */
public class CustomElements {


    public static Table getSubHeader(String reportName, List<ReportFilter> filterList) {

        Table table = new Table(filterList.size());
        table.setWidth(percent(100));

        table.addCell(new Cell(1, filterList.size()).add(getFilterCell(new ReportFilter("Reporte", reportName))));
        table.startNewRow();


        filterList.forEach((filter) -> table.addCell(getFilterCell(filter)));

        table.setMarginBottom(8);

        return table;
    }


    public static Cell getFilterCell(ReportFilter filter) {
        return new Cell().add(
                new Paragraph()
                        .add(new Text(String.format("%s: ", filter.getFilter())).setBold())
                        .add(new Text(filter.getValue() != null ? filter.getValue() : ""))

        );
    }

    public static UnitValue percent(int value) {
        return new UnitValue(UnitValue.PERCENT, value);
    }

    public static Table getHeader() {

        final String IMG_1 = "classpath:images/LogoAbel.png";
        final String IMG_2 = "classpath:images/vota4.png";
        Table headerTable = new Table(new UnitValue[]{
                UnitValue.createPointValue(45),
                UnitValue.createPointValue(400)
        }, false);
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setBorder(Border.NO_BORDER);
        headerTable.addCell(
                imageCell(IMG_1, 65)
                        .setPaddingLeft(0)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER)
        );

        headerTable.addCell(
                new Cell().add(
                        new Table(1)
                                .setWidth(UnitValue.createPercentValue(100))
                                .setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
                                .setBorder(Border.NO_BORDER)
                                .addCell(
                                        imageCell(IMG_2, 50, HorizontalAlignment.RIGHT)
                                                .setVerticalAlignment(VerticalAlignment.TOP)
                                                .setBorder(Border.NO_BORDER)
                                )
                                .addCell(
                                        new Cell()
                                                .add(
                                                        new Paragraph("MI PADRON DE ABEL")
                                                                .setFontColor(new DeviceRgb(6, 10, 63))
                                                                .setTextAlignment(TextAlignment.RIGHT)
                                                                .setFontSize(24)
                                                                .setBold())
                                                .setBorder(Border.NO_BORDER)

                                ))
                        .setBorder(Border.NO_BORDER)

        );

        headerTable.setMarginBottom(8);
        return headerTable;

    }


    public static Cell imageCell(String image, float height) {
        try {
            return new Cell()
                    //.setBorder(Border.NO_BORDER)
                    .add(
                            new Image(ImageDataFactory.create(image))
                                    .setHeight(height)
                    );
        } catch (MalformedURLException e) {
            return new Cell();
        }
    }

    public static Cell imageCell(String image, float height, HorizontalAlignment alignment) {
        try {
            return new Cell()
                    //.setBorder(Border.NO_BORDER)
                    .add(
                            new Image(ImageDataFactory.create(image))
                                    .setHeight(height)
                                    .setHorizontalAlignment(alignment)
                    );
        } catch (MalformedURLException e) {
            return new Cell();
        }
    }

    public static void applyWatermark(PdfDocument pdfDoc, Rectangle pageSize) {

        PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.2f);

        ImageData img = null;
        try {
            img = ImageDataFactory.create("classpath:images/logo.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        float w = img.getWidth();
        float h = img.getHeight();

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            try {


                PdfPage pdfPage = pdfDoc.getPage(i);

                // When "true": in case the page has a rotation, then new content will be automatically rotated in the
                // opposite direction. On the rotated page this would look as if new content ignores page rotation.
                pdfPage.setIgnorePageRotationForContent(true);

                float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
                PdfCanvas over = new PdfCanvas(pdfDoc.getPage(i));
                over.saveState();
                over.setExtGState(gs1);
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), false);

                over.restoreState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
