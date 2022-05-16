package com.example.robinhoodclinicpos;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    protected void onLoginButtonPressed() {
        String user = username.getText();
        String pass = password.getText();
        System.out.println(user+" "+pass);
        if (user.equals("admin") && pass.equals("admin")){
            System.out.println("Redirect to next page");
            try {
                FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
                //using the previous stage give some UI errors
                Stage stage = (Stage) username.getScene().getWindow();
                stage.close();

                Scene scene = new Scene(loader.load(), 1920, 1080);
//                stage.setTitle("RobinHood Clinic");
//                //resize icon because that didn't work
////        stage.getIcons().add(new Image("file:images/robinhoodicon(1).png"));
//                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/robinhoodicon(1).png")));
//                stage.setMaximized(true);
                stage.setScene(scene);
                stage.show();

            }catch (IOException io){
                io.printStackTrace();
            }
        }
        else{
            System.out.println("Wrong Username or Password. Try again.");
            username.setText("");
            password.setText("");
        }
    }
}