package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Deck;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;

@Service
public class DeckListWriter {

    @Autowired
    ResourceLoader resourceLoader;

    public void writeDeckList(Deck deck) throws FileNotFoundException, MalformedURLException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(deck.getSlug()+".pdf"));
        // 2 1/2 inch by 3 7/16
        Rectangle cardRectangle = new Rectangle(new BigDecimal(2.5*72).floatValue(),new BigDecimal(3.45*72).floatValue());
        PageSize cardSize = new PageSize(cardRectangle);
        Document document = new Document(pdf,cardSize);
        document.setMargins(0.2f,0.2f,0.2f,0.2f);
        document.setBorder(new SolidBorder(ColorConstants.BLACK,1));

        Table nameTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Cell nameCell = new Cell();
        nameCell.setFontSize(8);
        nameCell.setBorder(Border.NO_BORDER);
        nameCell.setMargin(0.1f);
        nameCell.setNextRenderer(new FitCellRenderer(nameCell,deck.getName(),true));
        nameTable.addCell(nameCell);
        for (Card card: deck.getCards()) {
            if (card.getKeywords().contains("hero")) {
                Cell heroCell = new Cell();
                heroCell.setFontSize(7);
                heroCell.setBorder(Border.NO_BORDER);
                heroCell.setMargin(0.1f);
                heroCell.add(new Paragraph(card.getName()));
                nameTable.addCell(heroCell);
            }
        }
        document.add(nameTable);

        UnitValue[] unitValues = new UnitValue[5];
        unitValues[0] = new UnitValue(2,5f);
        unitValues[1] = new UnitValue(2,5f);
        unitValues[2] = new UnitValue(2,15f);
        unitValues[3] = new UnitValue(2,70f);
        unitValues[4] = new UnitValue(2,5f);
        Table cardsTable = new Table(unitValues).useAllAvailableWidth();
        cardsTable.setMargin(0.01f);
        cardsTable.setPadding(0.01f);
        cardsTable.setExtendBottomRow(false);
        cardsTable.setExtendBottomRowOnSplit(false);
        Card equipHeader = new Card();
        equipHeader.setName("Equipment");
        equipHeader.setTotal(null);
        equipHeader.setRarity(" ");
        formatCard(cardsTable,equipHeader,true, true);
        for (Card card: deck.getCards()) {
            if (card.getKeywords().contains("equipment") ||
                card.getKeywords().contains("weapon")) {
                formatCard(cardsTable, card, true);
            }
        }
        Card cardHeader = new Card();
        cardHeader.setName("Cards");
        cardHeader.setTotal(null);
        cardHeader.setRarity(" ");
        formatCard(cardsTable,cardHeader,false, true);
        for (Card card: deck.getCards())
        {
            if (!card.getKeywords().contains("equipment") &&
                !card.getKeywords().contains("hero") &&
                !card.getKeywords().contains("weapon")) {
                formatCard(cardsTable, card, false);
            }
        }
        document.add(cardsTable);

        PdfDocument pdfDocument = document.getPdfDocument();
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++)
        {
            PdfPage page = pdfDocument.getPage(i);
            Rectangle pageRect = new Rectangle(page.getTrimBox());
            new PdfCanvas(page).setStrokeColor(ColorConstants.BLACK).setLineWidth(0.1f).rectangle(pageRect).stroke();
        }
        document.close();
    }

    private void formatCard(Table cardsTable, Card card, boolean equipment) throws MalformedURLException {
        formatCard(cardsTable, card, equipment, false);
    }

    private void formatCard(Table cardsTable, Card card, boolean equipment, boolean header) {
        Cell colorCell = new Cell();
        colorCell.setFontSize(6);
        colorCell.setBorder(Border.NO_BORDER);
        colorCell.setMargin(0.01f);
        colorCell.setPadding(0.01f);
        if (header) {
            colorCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
            colorCell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1));
            colorCell.setTextAlignment(TextAlignment.CENTER);
        }
        if (equipment) {
            colorCell.add(new Paragraph(""));
        } else {
            if (header)
                colorCell.add(new Paragraph("P"));
            else {
                if (card.getStats() != null && card.getStats().getResource() != null) {
                    Resource imageFile = resourceLoader.getResource(
                            "classpath:resource-"+card.getStats().getResource()+".png");
                    Image resourceCircle = null;
                    try {
                        resourceCircle = new Image(ImageDataFactory.create(Files.readAllBytes(imageFile.getFile().toPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (resourceCircle != null)
                        colorCell.add(resourceCircle.setAutoScale(true));
                }
                else
                    colorCell.add(new Paragraph(""));
            }
        }

        cardsTable.addCell(colorCell);

        Cell rarityCell = new Cell();
        rarityCell.setFontSize(6);
        rarityCell.setBorder(Border.NO_BORDER);
        rarityCell.setMargin(0.01f);
        rarityCell.setPadding(0.01f);
        if (header) {
            rarityCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
            rarityCell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1));
            rarityCell.add(new Paragraph("R"));
        } else {
            rarityCell.add(new Paragraph(card.getRarity().substring(0, 1)));
        }
        cardsTable.addCell(rarityCell);

        Cell idCell = new Cell();
        idCell.setFontSize(6);
        idCell.setBorder(Border.NO_BORDER);
        idCell.setMargin(0.01f);
        idCell.setPadding(0.01f);
        if (header) {
            idCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
            idCell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1));
            idCell.add(new Paragraph("ID"));
        } else {
            idCell.add(new Paragraph(card.getPrintings().get(0).getSku().getNumber()));
        }
        cardsTable.addCell(idCell);

        Cell cardNameCell = new Cell();
        cardNameCell.setFontSize(6);
        cardNameCell.setBorder(Border.NO_BORDER);
        cardNameCell.setMargin(0.01f);
        cardNameCell.setPadding(0.01f);
        if (header) {
            cardNameCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
            cardNameCell.setBorderBottom(new SolidBorder(ColorConstants.BLACK,1));
            cardNameCell.add(new Paragraph(new Text(card.getName()).setBold()));
        }
        else {
            cardNameCell.add(new Paragraph(card.getName()));
        }
        cardsTable.addCell(cardNameCell);

        Cell numberCell = new Cell();
        numberCell.setFontSize(6);
        numberCell.setBorder(Border.NO_BORDER);
        numberCell.setMargin(0.01f);
        numberCell.setPadding(0.01f);
        if (header) {
            numberCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
            numberCell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1));
            numberCell.add(new Paragraph("#"));
        }
        else {
            if (card.getTotal() != null)
                numberCell.add(new Paragraph(card.getTotal().toString()));
        }
        cardsTable.addCell(numberCell);
    }

    private static class FitCellRenderer extends CellRenderer {
        private String content;
        private boolean bold;

        public FitCellRenderer(Cell modelElement, String content, boolean bold) {
            super(modelElement);
            this.content = content;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new FitCellRenderer((Cell) modelElement, content, false);
        }

        /**
         * Method adapts content, that can't be fit into the cell,
         * to prevent truncation by replacing truncated part of content with '...'
         */
        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            PdfFont bf = getPropertyAsFont(Property.FONT);
            int contentLength = content.length();
            int leftChar = 0;
            int rightChar = contentLength - 1;

            Rectangle rect = layoutContext.getArea().getBBox().clone();

            // Cell's margins, borders and paddings should be extracted from the available width as well.
            // Note that this part of the sample was introduced specifically for iText7.
            // since in iText5 the approach of processing cells was different
            applyMargins(rect, false);
            applyBorderBox(rect, false);
            applyPaddings(rect, false);
            float availableWidth = rect.getWidth();

            UnitValue fontSizeUV = this.getPropertyAsUnitValue(Property.FONT_SIZE);

            // Unit values can be of POINT or PERCENT type. In this particular sample
            // the font size value is expected to be of POINT type.
            float fontSize = fontSizeUV.getValue();

            float totalSize = bf.getWidth(content,fontSize);
            if (totalSize <= availableWidth) {
                Text text = new Text(content);
                if (bold)
                    text.setBold();
                Paragraph p = new Paragraph(text);
                IRenderer pr = p.createRendererSubTree().setParent(this);
                childRenderers.add(pr);
                return super.layout(layoutContext);
            }

            availableWidth -= bf.getWidth("...", fontSize);

            while (leftChar < contentLength && rightChar != leftChar) {
                availableWidth -= bf.getWidth(content.charAt(leftChar), fontSize);
                if (availableWidth > 0) {
                    leftChar++;
                } else {
                    break;
                }

                availableWidth -= bf.getWidth(content.charAt(rightChar), fontSize);

                if (availableWidth > 0) {
                    rightChar--;
                } else {
                    break;
                }
            }

            // left char is the first char which should not be added
            // right char is the last char which should not be added
            String newContent = content.substring(0, leftChar) + "..." + content.substring(rightChar + 1);
            Text text = new Text(newContent);
            if (bold)
                text.setBold();
            Paragraph p = new Paragraph(text);

            // We're operating on a Renderer level here, that's why we need to process a renderer,
            // created with the updated paragraph
            IRenderer pr = p.createRendererSubTree().setParent(this);
            childRenderers.add(pr);

            return super.layout(layoutContext);
        }
    }
}
