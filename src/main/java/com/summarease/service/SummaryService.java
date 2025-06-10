package com.summarease.service;

import com.summarease.strategy.*;

public class SummaryService {

    public enum Method {
        RULE_BASED,
        API_BASED
    }

    private final Summarizer ruleBasedSummarizer;
    private final Summarizer apiBasedSummarizer;

    public SummaryService() {
        this.ruleBasedSummarizer = new RuleBasedSummarizer();
        this.apiBasedSummarizer = new ApiBasedSummarizer();
    }

    public String summarize(String text, Method method) {
        switch (method) {
            case RULE_BASED:
                return ruleBasedSummarizer.summarize(text);
            case API_BASED:
                return apiBasedSummarizer.summarize(text);
            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }

    public Summarizer getSummarizer(String method) {
        if ("Rule Based".equalsIgnoreCase(method)) {
            return ruleBasedSummarizer;
        } else if ("API based".equalsIgnoreCase(method)) {
            return apiBasedSummarizer;
        } else {
            throw new IllegalArgumentException("Unsupported summarization method: " + method);
        }
    }
}