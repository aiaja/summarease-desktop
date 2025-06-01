package com.summarease.strategy;
import java.util.*;
import java.util.stream.Collectors;



public class RuleBasedSummarizer implements Summarizer {

    @Override
    public String summarize(String text) {
        if (text == null || text.isBlank()) return "";

        String[] sentences = text.split("(?<=[.!?])\\s+");
        Map<String, Integer> wordFreq = new HashMap<>();

        for (String sentence : sentences) {
            for (String word : sentence.toLowerCase().split("\\W+")) {
                if (word.length() > 3) {
                    wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Score each sentence
        Map<String, Integer> sentenceScores = new LinkedHashMap<>();
        for (String sentence : sentences) {
            int score = 0;
            for (String word : sentence.toLowerCase().split("\\W+")) {
                score += wordFreq.getOrDefault(word, 0);
            }
            sentenceScores.put(sentence, score);
        }

        // Take top 3 sentences
        return sentenceScores.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(" "));
    }
}
