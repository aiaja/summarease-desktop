package com.summarease.model;
import java.time.LocalDateTime;



public class SummaryRecord {
    private final String originalText;
    private final String summarizedText;
    private final LocalDateTime timestamp;

    public SummaryRecord(String originalText, String summarizedText) {
        this.originalText = originalText;
        this.summarizedText = summarizedText;
        this.timestamp = LocalDateTime.now();
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getSummarizedText() {
        return summarizedText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
