package com.summarease.app;

import com.summarease.controller.MainController;
import com.summarease.model.UserPreferences;
import com.summarease.strategy.RuleBasedSummarizer;
import com.summarease.strategy.Summarizer;
import com.summarease.strategy.ApiBasedSummarizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        var fxmlUrl = getClass().getResource("/com/summarease/view/main_view.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML file not found: /com/summarease/view/main_view.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        UserPreferences preferences = new UserPreferences();

        Summarizer summarizer = "rule".equals(preferences.getDefaultMethod())
                ? new RuleBasedSummarizer()
                : new ApiBasedSummarizer();

        // Kirim summarizer ke controller
        MainController controller = loader.getController();
        controller.setSummarizer(summarizer);

        // Tambahkan CSS
        Scene scene = new Scene(root);
        var cssUrl = getClass().getResource("/com/summarease/style/main.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Warning: CSS file not found: /com/summarease/style/main.css");
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Summarease");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}