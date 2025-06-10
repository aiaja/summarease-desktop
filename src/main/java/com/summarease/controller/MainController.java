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
    private ComboBox<String> methodDropdown;

    @FXML
    public void initialize() {
        methodDropdown.getItems().addAll("Rule Based", "API based");
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
            outputTextArea.setText("Input text cannot be empty.");
            return;
        }

        if (selectedMethod == null) {
            outputTextArea.setText("Please select a summarization method.");
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
                outputTextArea.setText("Invalid summarization method selected.");
                return;
            }
            // res = [{"summary_text":"the afasfaffa is a fada based in syria . it is based

            outputTextArea.setText(result);
        } catch (Exception e) {
            outputTextArea.setText("An error occurred during summarization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClicked() {
        FileChooser fileChooser = new FileChooser(); // Initialize FileChooser
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        java.util.List<SummaryRecord> history = historyManager.getHistory();
        if (history.isEmpty()) {
            return;
        }
        SummaryRecord lastRecord = history.get(history.size() - 1);

        try {
            FileExporter.exportAsTxt(lastRecord, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
