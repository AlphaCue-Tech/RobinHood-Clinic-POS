package com.example.robinhoodclinicpos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerController {
    @FXML
    private TextField fullName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;

    @FXML
    private TextField documentId;
    public void setFullName(String s){
        fullName.setText(s);
    }

    public void setPhoneNumber(String s){
        phoneNumber.setText(s);
    }
    public void setAddress(String s){
        address.setText(s);
    }

    public boolean copyImagetoOfflineDB(String sourcePath, String destPath){
//        String src = "C:\\test\\testfile.txt";
        File original =new File(sourcePath);
        File destination =new File(destPath);

//        for(int x=0;destination.exists()==true;x++){
//            destination=new File("./Offline DB/Customer_Images/pic"+x+".jpg");
//
//        }
        try {
            Files.copy(original.toPath(),destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Successfully saved image to offlinedb");
            return true;
        } catch (Exception e) {
            System.out.println("Could not Save Image Offline DB");
            if (!original.exists()){
                System.out.println("Photo was not taken");
            }
            return false;
        }


    }
    public void deleteFile(File file){
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void createUnsyncedCustomer(){
        try {
            String srcPath = "src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg";
            String destPath = "./Offline DB/Customer_Images/"+phoneNumber.getText()+".jpg";
            if (!copyImagetoOfflineDB(srcPath, destPath)){
                throw new IOException();
            }
            FileWriter writer = new FileWriter("Offline DB/Unsynced_Customer.txt", true);
            writer.write(fullName.getText()+"//"+phoneNumber.getText()+"//"+address.getText()+"//"+Long.toString(System.currentTimeMillis())+"//"+destPath);
            writer.write("\r\n");   // write new line
            writer.close();
            File prevPhoto = new File("src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg");
            deleteFile(prevPhoto);
            Stage stage = (Stage) fullName.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Could Not Add customer in offline db");
        }
    }
    @FXML
    protected void sendUserDataToInvoice(){

        try {

            Firestore db = FirestoreClient.getFirestore();
            String firebasePhotoPath = "CUSTOMER_PHOTOS_FOLDER/" + phoneNumber.getText() + ".jpg";
            String myPhotoPath = "src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg";
//            DocumentReference docRef = db.collection("customers").document("alovelace");
// Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("name", fullName.getText());
            data.put("address", address.getText());
            data.put("phone", phoneNumber.getText());
            data.put("registered", System.currentTimeMillis());
            data.put("photoPath", firebasePhotoPath);
            ApiFuture<DocumentReference> ref = db.collection("customers").add(data);

            documentId.setText(ref.get().getId());
            StorageClient storageClient = StorageClient.getInstance();
            InputStream photo = new FileInputStream(myPhotoPath);

            storageClient.bucket().create(firebasePhotoPath, photo, Bucket.BlobWriteOption.userProject("robinhood-clinic"));
            Path path = FileSystems.getDefault().getPath(myPhotoPath);
            try {
                Files.delete(path);
            } catch (Exception x) {
                System.out.println("Error Deleting the file");
            }
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
            System.out.println("Saving to offline DB");
            createUnsyncedCustomer();
        }
    }
}
