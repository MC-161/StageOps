package com.operations.StageOps;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageListener implements ApplicationListener<JavafxApplication.StageReadyEvent> {


    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext applicationContext;

    public StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
                         @Value("classpath:/views/ui.fxml") Resource fxml, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.fxml = fxml;
        this.applicationContext = applicationContext;
    }
    @Override
    public void onApplicationEvent(@NonNull JavafxApplication.StageReadyEvent stageReadyEvent) {
        try {
            Stage stage = stageReadyEvent.getStage();
            URL url = fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            // Set window title
            stage.setTitle(this.applicationTitle);

            // Disable resizing, ensuring the window can't go full screen
            stage.setResizable(false);

            // Set max size to be the same as the initial window size
            stage.setMaxWidth(1400);
            stage.setMaxHeight(800);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}