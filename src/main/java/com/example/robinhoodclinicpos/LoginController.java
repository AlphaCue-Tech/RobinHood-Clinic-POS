package com.example.robinhoodclinicpos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection("users").get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (Exception e) {
           System.out.println("Error fetching data");
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("checking the login credentials");
            String un = document.getString("username");
            String pd = document.getString("password");
            System.out.println(un);
            System.out.println(pd);

            if (un.equals(user) && pd.equals(pass)){
                try {
                    String id = document.getId();

                    System.out.println("goes to the next page");
                    Map<String, Object> mp = new HashMap<>();
                    mp.put("username", un);
                    mp.put("password", pd);
                    mp.put("lastLogin", System.currentTimeMillis());
                    db.collection("users").document(id).set(mp);
                    FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
                    //using the previous stage give some UI errors
                    Stage stage = (Stage) username.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(loader.load(), 1920, 1080);
                    stage.setScene(scene);
                    stage.show();
                }
                catch(Exception e){
                    System.out.println("Login Ok. but could not redirect to the next page.");
                    System.out.println(e);
                }
            }
        }

        username.setText("");
        password.setText("");

        //uncomment to save data
//        if (user.equals("admin") && pass.equals("admin")){
//            System.out.println("Redirecting to invoice page");
//            try {
//                FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
//                //using the previous stage give some UI errors
//                Stage stage = (Stage) username.getScene().getWindow();
//                stage.close();
//
//                Scene scene = new Scene(loader.load(), 1920, 1080);
////                stage.setTitle("RobinHood Clinic");
////                //resize icon because that didn't work
//////        stage.getIcons().add(new Image("file:images/robinhoodicon(1).png"));
////                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/robinhoodicon(1).png")));
////                stage.setMaximized(true);
//                stage.setScene(scene);
//                stage.show();
//
//            }catch (IOException io){
//                io.printStackTrace();
//            }
//        }
//        else{
//
//            System.out.println("Wrong Username or Password. Try again.");
//            username.setText("");
//            password.setText("");
//        }
    }
}