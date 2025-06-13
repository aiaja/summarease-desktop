package com.summarease.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.IOException;

import com.summarease.data.SummaryHistoryManager;
import com.summarease.model.SummaryRecord;
import com.summarease.service.SummaryService;
import com.summarease.strategy.Summarizer;
import com.summarease.util.FileExporter;

import com.summarease.util.HistoryJsonUtil;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private final SummaryService summaryService = new SummaryService();

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextArea previewTextArea;

    @FXML
    private ComboBox<String> methodDropdown;

    @FXML
    public void initialize() {
        methodDropdown.getItems().addAll("Rule Based", "API based");

        // Load history dari file JSON saat aplikasi start
        java.util.List<SummaryRecord> loaded = com.summarease.util.HistoryJsonUtil.loadHistory();
        loaded.forEach(historyManager::addRecord);
    }

    private final SummaryHistoryManager historyManager = new SummaryHistoryManager();

    private Summarizer summarizer;

    public void setSummarizer(Summarizer summarizer) {
        this.summarizer = summarizer;
    }

    @FXML
    private void onSummarizeClicked() {
        String input = inputTextArea.getText();
        String selectedMethod = methodDropdown.getValue();

        if (input == null || input.trim().isEmpty()) {
            previewTextArea.setText("Input text cannot be empty.");
            return;
        }

        if (selectedMethod == null) {
            previewTextArea.setText("Please select a summarization method.");
            return;
        }

        try {
            String result;
            if ("Rule Based".equals(selectedMethod)) {
                result = summaryService.summarize(input, SummaryService.Method.RULE_BASED);
            } else if ("API based".equals(selectedMethod)) {
                // print the text
                result = summaryService.summarize(input, SummaryService.Method.API_BASED);
            } else {
                previewTextArea.setText("Invalid summarization method selected.");
                return;
            }
            // res = [{"summary_text":"the afasfaffa is a fada based in syria . it is based

            outputTextArea.setText(result);

            SummaryRecord record = new SummaryRecord(input, result);
            historyManager.addRecord(record);

            // Simpan ke JSON
            HistoryJsonUtil.saveHistory(historyManager.getHistory());

            // Show formatted preview
            previewTextArea.setText(com.summarease.util.SummaryFormatter.formatForExport(record));

        } catch (Exception e) {
            previewTextArea.setText("An error occurred during summarization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Summary");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        java.util.List<SummaryRecord> history = historyManager.getHistory();
        if (history.isEmpty()) {
            previewTextArea.setText("No summary to save.");
            return;
        }
        SummaryRecord lastRecord = history.get(history.size() - 1);

        String fileName = file.getName().toLowerCase();
        try {
            if (fileName.endsWith(".txt")) {
                FileExporter.exportAsTxt(lastRecord, file);
            } else if (fileName.endsWith(".pdf")) {
                FileExporter.exportAsPdf(lastRecord, file);
            } else {
                previewTextArea.setText("Unsupported file format selected.");
            }
        } catch (IOException e) {
            previewTextArea.setText("Failed to save file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onHistoryClicked() {
        java.util.List<SummaryRecord> history = com.summarease.util.HistoryJsonUtil.loadHistory();
        if (history.isEmpty()) {
            previewTextArea.setText("No history available.");
            return;
        }
        StringBuilder historyText = new StringBuilder();
        int count = 1;
        for (SummaryRecord record : history) {
            historyText.append("Record ").append(count++).append(":\n")
                    .append("Date: ").append(record.getTimestamp()).append("\n")
                    .append("Original Text:\n").append(record.getOriginalText()).append("\n")
                    .append("Summary:\n").append(record.getSummarizedText()).append("\n")
                    .append("-----\n");
        }
        previewTextArea.setText(historyText.toString());
    }

    @FXML
    private void onClearClicked() {
        inputTextArea.clear();
        previewTextArea.clear();
        outputTextArea.clear();
        methodDropdown.getSelectionModel().clearSelection();
        historyManager.clearHistory();
        previewTextArea.setText("History cleared.");
    }
}
