package com.strategy.api.reports.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.ByteArrayOutputStream;

public class TableHeaderEventHandler implements IEventHandler {
        protected Table table;
        protected float tableHeight;
        protected Document doc;

        public TableHeaderEventHandler(Document doc) {
            this.doc = doc;
            table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            table.setWidth(523);
            table.addCell("Header row 1");
            table.addCell("Header row 2");
            table.addCell("Header row 3");
            TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
            renderer.setParent(new Document(new PdfDocument(new PdfWriter(new ByteArrayOutputStream()))).getRenderer());
            tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox().getHeight();
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            Rectangle rect = new Rectangle(pdfDoc.getDefaultPageSize().getX() + doc.getLeftMargin(),
                    pdfDoc.getDefaultPageSize().getTop() - doc.getTopMargin(), 523, getTableHeight());
            new Canvas(canvas, pdfDoc, rect)
                    .add(CustomElements.getHeader());
        }

        public float getTableHeight() {
            return tableHeight;
        }
    }
