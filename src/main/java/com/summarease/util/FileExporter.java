package com.summarease.util;

import com.summarease.model.SummaryRecord;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FileExporter {

    public static void exportAsTxt(SummaryRecord record, File file) throws IOException {
        String formatted = SummaryFormatter.formatForExport(record);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(formatted);
        }
    }

    public static void exportAsPdf(SummaryRecord record, File file) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float width = page.getMediaBox().getWidth() - 2 * margin;
            float leading = 18;
            float x = margin;
            float y = yStart;

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Centered, bold "SUMMAREASE"
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 18);
            String title = "SUMMAREASE";
            float titleWidth = PDType1Font.TIMES_BOLD.getStringWidth(title) / 1000 * 18;
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, y);
            contentStream.showText(title);
            contentStream.endText();
            y -= leading * 2;

            // Centered, bold "Date:"
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            String dateLabel = "Date: "
                    + record.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            float dateWidth = PDType1Font.TIMES_BOLD.getStringWidth(dateLabel) / 1000 * 12;
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - dateWidth) / 2, y);
            contentStream.showText(dateLabel);
            contentStream.endText();
            y -= leading * 2;

            // Left-aligned, bold "Original Text:"
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            String origLabel = "Original Text:";
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(origLabel);
            contentStream.endText();
            y -= leading;

            // Justified original text (left-aligned for readability)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(x, y);
            y = writeMultiline(contentStream, record.getOriginalText(), width, leading, x, y);
            contentStream.endText();
            y -= leading;

            // Left-aligned, bold "Summary:"
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            String sumLabel = "Summary:";
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(sumLabel);
            contentStream.endText();
            y -= leading;

            // Justified summary text (left-aligned for readability)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(x, y);
            writeMultiline(contentStream, record.getSummarizedText(), width, leading, x, y);
            contentStream.endText();

            contentStream.close();
            document.save(file);
        }
    }

    /**
     * Helper to write multiline text with word wrap.
     */
    private static float writeMultiline(PDPageContentStream contentStream, String text, float width, float leading,
            float x, float y) throws IOException {
        String[] paragraphs = text.split("\n");
        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                float size = PDType1Font.TIMES_ROMAN.getStringWidth(testLine) / 1000 * 12;
                if (size > width) {
                    contentStream.showText(line.toString());
                    contentStream.newLineAtOffset(0, -leading);
                    y -= leading;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0)
                        line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                contentStream.showText(line.toString());
                contentStream.newLineAtOffset(0, -leading);
                y -= leading;
            }
        }
        return y;
    }
}
