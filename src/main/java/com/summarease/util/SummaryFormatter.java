package com.summarease.util;
import com.summarease.model.SummaryRecord;



public class SummaryFormatter {

    public static String formatForExport(SummaryRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== Ringkasan Teks ====\n");
        sb.append("Waktu   : ").append(record.getTimestamp()).append("\n\n");
        sb.append("Ringkasan:\n");
        sb.append(record.getSummarizedText()).append("\n\n");
        sb.append("=========================");
        return sb.toString();
    }
}
