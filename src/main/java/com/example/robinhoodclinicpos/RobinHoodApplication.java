package com.example.robinhoodclinicpos;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RobinHoodApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        InputStream serviceAccount = null;
        serviceAccount = new FileInputStream("robinhood-clinic-firebase-adminsdk-yzfpk-9aedc0fc60.json");

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setProjectId("robinhood-clinic").setStorageBucket("robinhood-clinic.appspot.com").build();
        FirebaseApp.initializeApp(options);
        FirebaseDatabase.getInstance("https://robinhood-clinic-default-rtdb.firebaseio.com/").setPersistenceEnabled(true);

        FXMLLoader fxmlLoader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
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