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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private TextField receptionistId;
    public void checkOfflineDatabase(){
        try {
            String user = username.getText();
            String pass = password.getText();

            FileReader reader = new FileReader("Offline DB/Login_DB.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String uname = line.split(" ")[0];
                String pwd = line.split(" ")[1];
                if (user.equals(uname) && pass.equals(pwd)){
                    try {
                        String id = line.split(" ")[2];

                        System.out.println("goes to the next page");
                        receptionistId.setText(id);
                        FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
                        //using the previous stage give some UI errors
                        Stage stage = (Stage) username.getScene().getWindow();

                        stage.close();
                        Scene scene = new Scene(loader.load(), 1920, 1080);
                        stage.setScene(scene);
                        InvoiceController c = loader.getController();
                        //Setting unsynced_login in database
                        writeToUnsyncedLogin(id, System.currentTimeMillis());

                        c.setNotSynced();
                        c.setReceptionistId(id);
                        stage.show();
                    }
                    catch(Exception e){
                        System.out.println("Login Ok. but could not redirect to the next page.");
                        System.out.println(e);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in offline");
        }
    }

    public void writeToUnsyncedLogin(String id, long t){
        try {
            FileWriter writer = new FileWriter("Offline DB/Unsynced_Login_Time.txt", true);
            writer.write(id+" "+Long.toString(t));
            writer.write("\r\n");   // write new line
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeLocalUserDB(List<QueryDocumentSnapshot> documents){
        try {
            FileWriter writer = new FileWriter("Offline DB/Login_DB.txt", false);
            for(QueryDocumentSnapshot document: documents){
                String un = document.getString("username");
                String pd = document.getString("password");
                String id = document.getId();

                writer.write(un+" "+pd+" "+id);
                writer.write("\r\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
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

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        //Setting up the local db for users
        if (documents.size()>0){
            changeLocalUserDB(documents);
        }

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
                    receptionistId.setText(id);
                    FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("invoice-view.fxml"));
                    //using the previous stage give some UI errors
                    Stage stage = (Stage) username.getScene().getWindow();

                    stage.close();
                    Scene scene = new Scene(loader.load(), 1920, 1080);
                    stage.setScene(scene);
                    InvoiceController c = loader.getController();
                    c.setReceptionistId(id);
                    stage.show();
                }
                catch(Exception e){
                    System.out.println("Login Ok. but could not redirect to the next page.");
                    System.out.println(e);
                }
            }
        }
        } catch (Exception e) {
            System.out.println("Error fetching data");
            System.out.println("Checking the offline database");
            checkOfflineDatabase();
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