package com.summarease.util;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.summarease.model.SummaryRecord;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryJsonUtil {
    private static final String HISTORY_FILE = "history.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void saveHistory(List<SummaryRecord> history) {
        try (Writer writer = new FileWriter(HISTORY_FILE)) {
            gson.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<SummaryRecord> loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists())
            return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<SummaryRecord>>() {
            }.getType();
            List<SummaryRecord> history = gson.fromJson(reader, listType);
            return history != null ? history : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}