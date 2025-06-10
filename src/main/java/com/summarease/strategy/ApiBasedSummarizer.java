package com.summarease.strategy;

import com.summarease.util.HttpClientHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiBasedSummarizer implements Summarizer {

    private static final String API_URL = "https://api-inference.huggingface.co/models/Falconsai/text_summarization";
    private static final String API_TOKEN;

    static {
        Properties props = new Properties();
        try (InputStream is = ApiBasedSummarizer.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
                API_TOKEN = props.getProperty("huggingface.api_token", "");
            } else {
                throw new RuntimeException("config.properties not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    @Override
    public String summarize(String text) {
        // Properly escape JSON string
        String safeText = text
                .replace("\\", "\\\\")
                .replace("\"", "'")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        String payload = "{\"inputs\": \"" + safeText + "\"}";

        String response = HttpClientHelper.post(API_URL, API_TOKEN, payload);

        // Parse JSON response for summary_text or error
        if (response == null || response.trim().isEmpty()) {
            return "API Error: Empty response";
        }
        response = response.trim();
        // print the response for debugging
        System.out.println("API Response: " + response);
        try {
            if (response.startsWith("{") && response.contains("\"error\"")) {

                JSONObject errorObj = new JSONObject(response);
                return "API Error: " + errorObj.optString("error", "Unknown error");
            }
            if (response.startsWith("[")) {
                JSONArray arr = new JSONArray(response);
                if (arr.length() > 0) {
                    JSONObject obj = arr.getJSONObject(0);
                    return obj.optString("summary_text", response);
                }
            }
        } catch (Exception e) {
            return "API Error: Invalid response format";
        }
        return response;
    }
}