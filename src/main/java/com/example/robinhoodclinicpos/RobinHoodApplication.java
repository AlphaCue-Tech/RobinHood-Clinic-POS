package com.example.robinhoodclinicpos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RobinHoodApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RobinHoodApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("RobinHood Clinic");
        //resize icon because that didn't work
//        stage.getIcons().add(new Image("file:images/robinhoodicon(1).png"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/robinhoodicon(1).png")));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}