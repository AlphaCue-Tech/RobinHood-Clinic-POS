package com.example.robinhoodclinicpos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FindCustomerViewController {
    @FXML
    private TextField fullName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    @FXML
    protected void findUser(){
        System.out.println("Edit Button Pressed");
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference customers = db.collection("customers");

            Query query = customers.whereEqualTo("phone", phoneNumber.getText());
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                fullName.setText(document.getString("name"));
                address.setText(document.getString("address"));
                break;
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error Finding old Customer!");
        }
    }
    @FXML
    protected void closeDialog(){
        Stage stage = (Stage) phoneNumber.getScene().getWindow();
        stage.close();
    }
}
