package com.example.robinhoodclinicpos;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerController {
    @FXML
    private TextField fullName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;

    public void setFullName(String s){
        fullName.setText(s);
    }

    public void setPhoneNumber(String s){
        phoneNumber.setText(s);
    }
    public void setAddress(String s){
        address.setText(s);
    }
    @FXML
    protected void sendUserDataToInvoice(){
        Stage stage = (Stage) fullName.getScene().getWindow();
        stage.close();
    }
}
