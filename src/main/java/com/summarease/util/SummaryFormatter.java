package com.summarease.util;

import com.summarease.model.SummaryRecord;
import java.time.format.DateTimeFormatter;

public class SummaryFormatter {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String formatForExport(SummaryRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("SUMMAREASE\n\n");
        sb.append("Date : ").append(record.getTimestamp().format(TIMESTAMP_FORMATTER)).append("\n\n");
        sb.append("Original Text :\n");
        sb.append(record.getOriginalText()).append("\n\n");
        sb.append("Summary :\n");
        sb.append(record.getSummarizedText()).append("\n");
        return sb.toString();
    }
}
