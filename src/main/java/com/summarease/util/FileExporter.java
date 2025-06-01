package com.summarease.util;
import com.summarease.model.SummaryRecord;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




public class FileExporter {

    public static void exportAsTxt(SummaryRecord record, File file) throws IOException {
        String formatted = SummaryFormatter.formatForExport(record);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(formatted);
        }
    }

    // Placeholder: export ke PDF (gunakan iText / Apache PDFBox jika ingin)
    public static void exportAsPdf(SummaryRecord record, File file) throws IOException {
        throw new UnsupportedOperationException("PDF export belum diimplementasikan");
    }
}
