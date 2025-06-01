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
        UserPreferences preferences = new UserPreferences();

        Summarizer summarizer = preferences.getDefaultMethod().equals("rule")
                ? new RuleBasedSummarizer()
                : new ApiBasedSummarizer();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();

        // Kirim summarizer ke controller
        MainController controller = loader.getController();
        controller.setSummarizer(summarizer);

        //Tambahkan CSS
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Summarease");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}