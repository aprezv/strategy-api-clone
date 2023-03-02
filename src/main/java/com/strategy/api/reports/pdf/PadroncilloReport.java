package com.strategy.api.reports.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.strategy.api.base.exceptions.RestResponseException;
import com.strategy.api.base.model.jpa.BaseEnum;
import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.persistence.respositories.CallResponse;
import com.strategy.api.persistence.respositories.CallStatus;
import com.strategy.api.persistence.respositories.PadroncilloResponse;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created on 9/2/22.
 */
public class PadroncilloReport {

    public static Resource generateReport(PadroncilloResponse simpatizante, List<PadroncilloResponse> padroncillo) {
        PdfFont font;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos, new WriterProperties().setFullCompressionMode(true)));
        pdfDocument.getWriter().setCompressionLevel(CompressionConstants.BEST_COMPRESSION);

        Document document = new Document(pdfDocument, PageSize.A4);
        document.setMargins(10f, 10f, 10f, 10f);
        try {

            font = PdfFontFactory.createFont("fonts/arial.ttf", PdfEncodings.IDENTITY_H, true);
            document.setFont(font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.add(CustomElements.getHeader().setBorder(Border.NO_BORDER));
        document.add(
                new Paragraph()
                        .add(new Text("MULTIPLICADOR: ").setBold())
                        .setMarginBottom(0)
        );

        document.add(getSubHeader(simpatizante));


        document.add(
                new Paragraph()
                        .add(new Text("SIMPATIZATES: ").setBold())
                        .add(new Text(String.valueOf(padroncillo.size())))
                        .setMarginBottom(0)
        );
        document.add(getBoxes(padroncillo));


        document.close();

        return new ByteArrayResource(baos.toByteArray());
    }


    private static Table getBoxes(List<PadroncilloResponse> simpatizantes) {

        Table table = new Table(2);
        table.setWidth(new UnitValue(UnitValue.PERCENT, 100));

        int count = 1;
        simpatizantes.forEach((sympathizer -> addSympathizerBox(table, sympathizer, count)));

        return table;
    }


    private static Paragraph getText(String text) {
        if (text == null)
            text = "";
        return new Paragraph(new Text(text).setFontSize(8));
    }


    public static Table getSubHeader(PadroncilloResponse multi) {

        Table headerTable = new Table(new UnitValue[]{
                UnitValue.createPercentValue(50),
                UnitValue.createPercentValue(50)
        }, false);
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setBorder(Border.NO_BORDER);
        headerTable.setPadding(0);
        headerTable.setMargin(0);

        headerTable.addCell(
                new Cell().add(getBox(multi))
                        .setBorder(Border.NO_BORDER)
        );

        if (!StringUtils.isEmpty(multi.getCedula_coordinador())) {
            headerTable.addCell(
                    new Cell().add(getCoord(multi))
                            .setBorder(Border.NO_BORDER)
            );
        }

        headerTable.setMarginBottom(8);
        return headerTable;

    }

    private static Table addSympathizerBox(Table table, PadroncilloResponse sympathizer, int count) {

        Table mainTable = getBox(sympathizer);

        table.addCell(new Cell().add(mainTable).setBorder(Border.NO_BORDER));
        if (count % 2 == 0) {
            mainTable.startNewRow();
        }
        return mainTable;
    }

    public static Table getBox(PadroncilloResponse sympathizer) {
        Table mainTable = new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 10), new UnitValue(UnitValue.PERCENT, 90)});
        mainTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        mainTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
        Table imageTable = new Table(1);
        imageTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
        Table detailTable = new Table(1);
        detailTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        detailTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);


        detailTable.addCell(
                new Cell()
                        .add(
                                new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 80), new UnitValue(UnitValue.PERCENT, 20)})
                                        .setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
                                        .setBorder(Border.NO_BORDER)
                                        .addCell(
                                                new Cell()
                                                        .add(getText(sympathizer.getNombre()).setBold())
                                                        .setBorder(Border.NO_BORDER)
                                        )

                                        .addCell(new Cell()
                                                .add(getText(sympathizer.getCedula())
                                                        .setBorder(Border.NO_BORDER))
                                                .setBorder(Border.NO_BORDER)
                                                .setBorderLeft(new SolidBorder(1))
                                        )
                        ).setPadding(0)
                        .setMargin(0)
        );

        detailTable.startNewRow();

        detailTable.addCell(
                new Cell()
                        .add(
                                new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 50), new UnitValue(UnitValue.PERCENT, 50)})
                                        .setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
                                        .setBorder(Border.NO_BORDER)
                                        .addCell(new Cell()
                                                .add(getText(String.format("Tel: %s", sympathizer.getTelefono())))
                                                .setBorder(Border.NO_BORDER)
                                        )

                                        .addCell(new Cell()
                                                .add(getText(String.format("WA: %s", sympathizer.getWhats_app()))
                                                        .setBorder(Border.NO_BORDER))
                                                .setBorder(Border.NO_BORDER)
                                                .setBorderLeft(new SolidBorder(1))
                                        )
                        ).setPadding(0)
                        .setMargin(0)
        );

        detailTable.startNewRow();

        detailTable.addCell(
                new Cell()
                        .add(
                                new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 100)})
                                        .setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
                                        .setBorder(Border.NO_BORDER)
                                        .addCell(new Cell()
                                                .add(
                                                        getText(

                                                                String.format(
                                                                        "%s%s%s",
                                                                        Optional.ofNullable(sympathizer.getColegio())
                                                                                .map((c) -> "CC: ")
                                                                                .orElse(""),
                                                                        sympathizer.getRecinto().trim(),
                                                                        Optional.ofNullable(sympathizer.getColegio())
                                                                                .map(c -> String.format(" - MESA %s", c))
                                                                                .orElse("")
                                                                )

                                                        ))
                                                .setBorder(Border.NO_BORDER)
                                        )
                        ).setPadding(0)
                        .setMargin(0)
        );

        detailTable.startNewRow();


        String direccion = sympathizer.getColegio() == null ? "N/D" :
                sympathizer.getDireccion().trim() + ", "
                        + sympathizer.getSector().trim() + ", "
                        + sympathizer.getMunicipio().trim() + ", "
                        + sympathizer.getProvincia().trim();

        detailTable.addCell(
                new Cell()
                        .add(
                                new Table(
                                        new UnitValue[]{new UnitValue(UnitValue.PERCENT, 100)})
                                        .setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
                                        .setBorder(Border.NO_BORDER)
                                        .addCell
                                                (new Cell()
                                                        .add(getText(direccion)
                                                                .setBorder(Border.NO_BORDER))
                                                        .setBorder(Border.NO_BORDER)
                                                )
                        ).setPadding(0)
                        .setMargin(0)
        );

        Image image;
        try {
            image = Optional.ofNullable(sympathizer.getFoto())
                    .map(i -> new Image(ImageDataFactory.create(i)))
                    .orElse(new Image(ImageDataFactory.create("classpath:images/nd.png")));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error generando imagen");
        }


        Cell imageCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(image.setHeight(72f).setWidth(54f));

        if (BooleanUtils.isTrue(sympathizer.getPld())) {
            Image pld;
            try {
                pld = new Image(ImageDataFactory.create("classpath:images/pld.png"));
                imageCell.add(pld.setRelativePosition(0,-72,0,0).setHeight(14).setWidth(10));
            } catch (Exception ignore) {
                throw new RuntimeException("Error generando imagen");
            }
        }

        imageCell.setPadding(0)
                .setMargin(0);

        imageTable.addCell(imageCell);

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

        return mainTable;
    }

    public static Table getCoord(PadroncilloResponse sympathizer) {
        Table mainTable = new Table(new UnitValue[]{new UnitValue(UnitValue.PERCENT, 100)});
        mainTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
        mainTable.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE);
        mainTable.addCell(new Cell().add(getText("COORDINADOR DE CONTACTO").setBold()));
        mainTable.addCell(new Cell().add(getText(sympathizer.getCoordinador())));
        mainTable.addCell(new Cell().add(getText("CEDULA: " + sympathizer.getCedula_coordinador())));
        mainTable.addCell(new Cell().add(getText("TELEFONO: " + sympathizer.getTelefono_coordinador())));
        mainTable.addCell(new Cell().add(getText("WHATSAPP: " + sympathizer.getWhatsapp_coordinador())));
        return mainTable;
    }

}
