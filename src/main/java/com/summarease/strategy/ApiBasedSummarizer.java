package com.summarease.strategy;

public class ApiBasedSummarizer implements Summarizer {

    @Override
    public String summarize(String text) {
        // Dummy response, bisa diganti nanti dengan call ke API nyata
        return "Ringkasan dari API akan muncul di sini (dummy).";
    }
}
