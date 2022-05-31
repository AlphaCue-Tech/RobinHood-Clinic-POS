package com.example.robinhoodclinicpos;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AdditionalCostController {
    @FXML
    private TextField costDescription;
    @FXML
    private TextField cost;

    private ArrayList<String> costDescriptionList;
    private ArrayList<Double> costList;


    @FXML
    protected void addCostButtonPressed(){
        costList.add(Double.parseDouble(cost.getText()));
        costDescriptionList.add(costDescription.getText());
        cost.setText("");
        costDescription.setText("");
    }
    @FXML
    protected void doneButtonPressed(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Finished for Today?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            for(int i = 0; i<costList.size(); i++){
                System.out.println(costList.get(i));
                System.out.println(costDescriptionList.get(i));
            }
            //Get all invoices of today from database

            //Generate the daily report from all that data
            Stage stage = (Stage) cost.getScene().getWindow();
            stage.close();
        }
    }
    public void initialize() {
        costDescriptionList = new ArrayList<String>();
        costList = new ArrayList<Double>();
    }

}
