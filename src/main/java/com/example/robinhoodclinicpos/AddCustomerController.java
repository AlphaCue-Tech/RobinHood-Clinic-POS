package com.example.robinhoodclinicpos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

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

        try {

            Firestore db = FirestoreClient.getFirestore();

//            DocumentReference docRef = db.collection("customers").document("alovelace");
// Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("name", fullName.getText());
            data.put("address", address.getText());
            data.put("phone", phoneNumber.getText());
            data.put("registered", System.currentTimeMillis());
            ApiFuture<DocumentReference> ref = db.collection("customers").add(data);

//              Map map = new HashMap();
//            map.put("timestamp", ServerValue.TIMESTAMP);
//            ref.child("yourNode").updateChildren(map);

//asynchronously write data
//            ApiFuture<WriteResult> result = docRef.set(data);
// ...
// result.get() blocks on response
//            System.out.println("Update time : " + result.get().getUpdateTime());
            System.out.println(ref.get());
            Stage stage = (Stage) fullName.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error Adding new Customer!");
        }
    }
}
