package com.summarease.controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.IOException;

import com.summarease.data.SummaryHistoryManager;
import com.summarease.model.SummaryRecord;
import com.summarease.strategy.Summarizer;
import com.summarease.util.FileExporter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class MainController {
    
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ComboBox<String> methodDropdown;

    private final SummaryHistoryManager historyManager = new SummaryHistoryManager();



    private Summarizer summarizer;

    public void setSummarizer(Summarizer summarizer) {
        this.summarizer = summarizer;
    }

    @FXML
    private void onSummarizeClicked() {
        String input = inputTextArea.getText();
        String result = summarizer.summarize(input);
        outputTextArea.setText(result);
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

