package com.example.robinhoodclinicpos;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

class CustomCell extends ListCell<String> {
    private Button actionBtn;
    private Label name ;
    private GridPane pane ;
    private TextField amt;

    public CustomCell() {
        super();

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //do something
            }
        });

        actionBtn = new Button("Add this");
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Action: "+getItem()+"Amount: "+ amt.getText());
            }
        });
        name = new Label();
        pane = new GridPane();
        amt = new TextField();
        pane.add(name, 0, 0);
//        pane.add(amt, 1, 0);
//        pane.add(actionBtn, 2, 0);
//        name.setAlignment(Pos.CENTER);
//        amt.setAlignment(Pos.CENTER);
//        actionBtn.setAlignment(Pos.CENTER);
        pane.setHgap(40.0);
        setText(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null) {
            name.setText(item);
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}


class itemCustomCell extends ListCell<String> {
    private Button actionBtn;
    private Label name ;
    private GridPane pane ;
    private TextField amt;
    public itemCustomCell() {
        super();

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //do something
            }
        });

        actionBtn = new Button("Remove");

        name = new Label();
        pane = new GridPane();
        amt = new TextField();
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                System.out.println("Action: "+getItem()+"Amount: "+ amt.getText());
                pane = null;
            }
        });
        pane.add(name, 0, 0);
//        pane.add(amt, 1, 0);
//        pane.add(actionBtn, 1, 0);
//        name.setAlignment(Pos.CENTER);
//        amt.setAlignment(Pos.CENTER);
//        actionBtn.setAlignment(Pos.CENTER);
        pane.setHgap(40.0);
        setText(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null) {
            name.setText(item);
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}
public class InvoiceController {
    @FXML
    private ListView<String> itemListView;
    @FXML
    private ListView<String> customerItemListView;
    @FXML
    private TextField itemCount;
    @FXML
    private CheckBox cashCheckBox;
    @FXML
    CheckBox bKashCheckBox;
    @FXML
    private CheckBox nogodCheckBox;
    @FXML
    private CheckBox cardCheckBox;
    @FXML
    private CheckBox otherCheckBox;
    @FXML
    private Label totalBill;
    @FXML
    private ImageView webcamPhoto;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label customerName;
    @FXML
    private VBox itemCountVBox;

    private String referenceNo;
    private boolean cameraOn=false;
    private String customerDocumentID;

    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private ObservableList<String> customerItemObservableList = FXCollections.observableArrayList();
    private ArrayList<Double> costList;
    private ArrayList<String> boughtItemNameList;
    private ArrayList<Integer> boughtItemQuantityList;

    private ArrayList<String> boughtItemIDList;
    private ArrayList<String> itemName;
    private ArrayList<Double> itemCost;

    private ArrayList<String> itemID;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Stage addCustomerStage;
    private TextField refField;
    private String receptionistId;

    private String paymentMethod;

    ArrayList<String> invoiceHelperPhoneNumber;
    ArrayList<String> invoiceHelperCustomerRef;
    Map<String, String> invoiceHelperRefPhone;
    Thread taskThread;
    Thread syncThread;
    Webcam webcam;

    boolean notSynced = false;
    public void setNotSynced(){
        notSynced = true;
        startSyncThread();
    }
    public String getFullName(){
        return fullName;
    }
    public void setReceptionistId(String s){ receptionistId = s; }

    public void webcamLiveView(){

//        while(true) {
//            System.out.println("Taking an iamge for liveview");
            try {
                BufferedImage image = webcam.getImage();
                Image im = SwingFXUtils.toFXImage(image, null);
                webcamPhoto.setImage(im);
            }
            catch(Exception e){
            }
    }

    @FXML
    protected void dailySummaryButtonPressed(){
        //Redirect to Additional Cost Page
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Finished for Today?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                stopCamera();
                FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("additionalCost-view.fxml"));
                //using the previous stage give some UI errors
                Stage stage = (Stage) totalBill.getScene().getWindow();
                stage.close();

                Scene scene = new Scene(loader.load(), 800, 600);
                stage.setTitle("RobinHood Clinic");
//                //resizing icon because that didn't work
////        stage.getIcons().add(new Image("file:images/robinhoodicon(1).png"));
//                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/robinhoodicon(1).png")));
//                stage.setMaximized(true);
                stage.setScene(scene);
                AdditionalCostController c =  loader.getController();
                c.setReceptionistId(receptionistId);

                stage.show();

            }catch (IOException io){
                io.printStackTrace();
            }
        }

    }
    public void stopCamera(){
        cameraOn = false;
        if (taskThread != null) {
            taskThread.interrupt();
            taskThread.stop();
            taskThread = null;
        }
        if (webcam != null) {
            webcam.close();
        }
        File file = new File("src/main/resources/com/example/robinhoodclinicpos/images/cameraOff.png");
        Image image = new Image(file.toURI().toString());

        webcamPhoto.setImage(image);

    }
    @FXML
    protected void onSwitchCameraButtonPressed(){

        if(cameraOn){
            stopCamera();
            cameraOn = false;
        }
        else{
            startCamera();
            startWebcamThread();
            cameraOn = true;
        }
    }
    @FXML
    protected void onTakePhotoButtonPressed(){
//        webcam.open();
//// to the job ...
//        webcam.close();
//        webcam.open();
        if(taskThread != null) {
            taskThread.interrupt();

        }
        Dimension resolution = new Dimension(1920, 1080);// 1080p
       BufferedImage image = webcam.getImage();

        try {
            ImageIO.write(image, ImageUtils.FORMAT_JPG, new File("src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg"));

//            Thread.sleep(2000);
            Image im = SwingFXUtils.toFXImage(image, null);
            webcamPhoto.setImage(im);
            Thread.sleep(2000);
        } catch (Exception e) {

            System.out.println("Could not capture image");
            System.out.println(e);
        }
        finally {
            //not closing the webcam makes for faster image capturing
//            webcam.close();

            if(taskThread != null) {
                taskThread.start();
            }
        }


        //Takes a photo but it doesn't show up. can probably save it to the database. also there is no control for taking the photo
//        try {
//            FrameGrabber grabber = new OpenCVFrameGrabber(0);
//            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//            grabber.start();
//            Frame frame = grabber.grab();
//            IplImage img = converter.convert(frame);
////            cvSaveImage("src/main/resources/com/example/robinhoodclinicpos/images/selfie.png", img);
//
//            cvSaveImage("selfie.png", img);
//            Thread.sleep(3000);
////            webcamPhoto.setImage(new Image(getClass().getResourceAsStream("images/selfie.png")));
//
//            webcamPhoto.setImage(new Image(getClass().getResourceAsStream("../../../../selfie.png")));
//        } catch(Exception e){
//            System.out.println(e);
//            System.out.println("Encountered an error while taking webcam photo");
//        }
    }
    public void createInvoiceInDatabase(){

        try{
            if (!checkInternetConnection()){
                throw new Exception();
            }
            Map<String, Integer> items = new HashMap<String, Integer>();
            for(int i = 0; i<boughtItemIDList.size(); i++){
                items.put(boughtItemIDList.get(i), boughtItemQuantityList.get(i));
            }
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> data = new HashMap<>();
            data.put("invoiceBy", db.collection("users").document(receptionistId));
            data.put("discount", 0);
            data.put("items", items);
            data.put("customerId", db.collection("customers").document(customerDocumentID));
            data.put("paymentMethod", paymentMethod);
            if (paymentMethod != "Cash"){
                if(paymentMethod == "Other"){
                    data.put("Other", refField.getText());
                }
                else{
                    data.put("paymentRef", refField.getText());
                }
            }
            data.put("totalBill", Double.parseDouble(totalBill.getText()));
            data.put("time", System.currentTimeMillis());
            ApiFuture<DocumentReference> ref = db.collection("invoices").add(data);
            String invoiceId = ref.get().getId();

            System.out.println(invoiceId);
        } catch (Exception e) {
            System.out.println("Invoice wasn't created in online Database");
            System.out.println("Creating the offline database");
            createUnsyncedInvoice();
            return;
        }
    }


    public void generateInvoicePDF(){
        String payRef = "";
        if (paymentMethod != "Cash"){
            if(paymentMethod == "Other"){
                payRef = refField.getText();
            }
            else{
                payRef = refField.getText();
            }
        }
        new InvoicePdfGenerator(fullName, phoneNumber, address, paymentMethod, payRef, boughtItemNameList, boughtItemQuantityList, costList, totalBill.getText());
    }
    @FXML
    protected void generateInvoicePressed(){
        //Save to database first, if error don't continue
        //Create Invoice in Database
        createInvoiceInDatabase();
        generateInvoicePDF();
        stopCamera();
        //TODO: print pdf
    }
    @FXML
    public void showRef(String s){
        if (refField == null) {
            refField = new TextField();
            itemCountVBox.getChildren().add(0, refField);
        }
        refField.setPromptText(s);
    }
    @FXML
    protected void cashChecked(){
        if (cashCheckBox.isSelected()){
            paymentMethod = "Cash";
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
            if (refField != null){
                refField=null;
                itemCountVBox.getChildren().remove(0);
            }
        }

    }

    @FXML
    protected void bKashChecked(){
        if (bKashCheckBox.isSelected()){
            paymentMethod = "bKash";
            cashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }
        showRef("Reference No.");
    }
    @FXML
    protected void nogodChecked(){
        if (nogodCheckBox.isSelected()){
            paymentMethod = "Nogod";
            bKashCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }
        showRef("Reference No.");
    }
    @FXML
    protected void cardChecked(){
        if (cardCheckBox.isSelected()){
            paymentMethod = "Card";
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }
        showRef("Reference No.");
    }
    @FXML
    protected void otherChecked(){
        if (otherCheckBox.isSelected()){
            paymentMethod = "Other";
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
        }
        showRef("Other");
    }
    @FXML
    protected void findCustomerButtonPressed(){
        System.out.println("Finding an old customer");
        try {
            FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("findCustomer-view.fxml"));
            addCustomerStage = new Stage();
            addCustomerStage.setTitle("Find Customer");
            addCustomerStage.setScene(new Scene(loader.load(), 800, 600));

            addCustomerStage.showAndWait();

            TextField temp;
            temp = (TextField) loader.getNamespace().get("fullName");
            fullName = temp.getText();

            temp = (TextField) loader.getNamespace().get("phoneNumber");
            phoneNumber = temp.getText();
            temp = (TextField) loader.getNamespace().get("address");
            address = temp.getText();
            temp = (TextField) loader.getNamespace().get("documentId");
            customerDocumentID = temp.getText();
            customerName.setText(fullName);
            // Hide this current window (if this is what you want)
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void editCustomerButtonPressed(){
        System.out.println("Editing a new customer");
        try {
            FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("addCustomer-view.fxml"));
            addCustomerStage = new Stage();
            addCustomerStage.setTitle("Edit Customer");
            addCustomerStage.setScene(new Scene(loader.load(), 800, 600));
            AddCustomerController c =  loader.getController();
            c.setFullName(fullName);
            c.setPhoneNumber(phoneNumber);
            c.setAddress(address);
            addCustomerStage.showAndWait();

            TextField temp;
            temp = (TextField) loader.getNamespace().get("fullName");
            fullName = temp.getText();

            temp = (TextField) loader.getNamespace().get("phoneNumber");
            phoneNumber = temp.getText();
            temp = (TextField) loader.getNamespace().get("address");
            address = temp.getText();
            customerName.setText(fullName);
            // Hide this current window (if this is what you want)
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    protected void addCustomerButtonPressed(){

        System.out.println("Adding a new customer");
        try {
            FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("addCustomer-view.fxml"));

            addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(loader.load(), 800, 600));
            addCustomerStage.showAndWait();

            TextField temp;
            temp = (TextField) loader.getNamespace().get("fullName");
            fullName = temp.getText();
            temp = (TextField) loader.getNamespace().get("phoneNumber");
            phoneNumber = temp.getText();
            temp = (TextField) loader.getNamespace().get("address");
            address = temp.getText();
            temp = (TextField) loader.getNamespace().get("documentId");
            customerDocumentID = temp.getText();
            System.out.println(customerDocumentID);
            customerName.setText(fullName);
            // Hide this current window (if this is what you want)
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    protected void logOutButtonPressed(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("login-view.fxml"));
                //using the previous stage give some UI errors
                Stage stage = (Stage) totalBill.getScene().getWindow();
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
    }

    @FXML
    protected void addItemButtonPressed(){
        try {
            int quantity = Integer.parseInt(itemCount.getText());
            if (quantity <= 0) return;
            System.out.println(quantity);
            int itemIndex = itemListView.getSelectionModel().getSelectedIndex();
            String currentItemName = itemName.get(itemIndex);
            double currentItemCost = itemCost.get(itemIndex);
            String currentItemID = itemID.get(itemIndex);
            System.out.println(currentItemName);
            System.out.println(currentItemCost*quantity);
            customerItemObservableList.add("Item: "+currentItemName+" Quantity: "+Integer.toString(quantity)+" Cost: "+Double.toString(currentItemCost*quantity));
            customerItemListView.setItems(customerItemObservableList);
            customerItemListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> stringListView) {
                    return new itemCustomCell();
                }
            });
            costList.add(currentItemCost*quantity);
            boughtItemQuantityList.add(quantity);
            boughtItemNameList.add(currentItemName);
            boughtItemIDList.add(currentItemID);
            double prev = Double.parseDouble(totalBill.getText());
            prev += currentItemCost*quantity;
            totalBill.setText(Double.toString(prev));
            itemCount.setText("1");

        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Not a number");
            return;
        }

    }
    @FXML
    protected void removeItemButtonPressed(){
        if (customerItemListView.getSelectionModel().getSelectedIndex() == -1) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this item?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        try {
            int itemIndex = customerItemListView.getSelectionModel().getSelectedIndex();
            if (itemIndex >= 0) {
                customerItemListView.getItems().remove(itemIndex);
                double p = costList.get(itemIndex);
                costList.remove(itemIndex);

                boughtItemQuantityList.remove(itemIndex);
                boughtItemNameList.remove(itemIndex);
                boughtItemIDList.remove(itemIndex);
                double prev = Double.parseDouble(totalBill.getText());
                prev -= p;
                totalBill.setText(Double.toString(prev));
            }

        }
        catch(Exception e){
            System.out.println("Wrong Index");
            return;
        }

    }

    @FXML
    protected void searchItem(){
        try {
            observableList.clear();
            System.out.println(observableList.size());
            for (int i = 0; i < itemName.size(); i++) {
                if (itemName.get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
                    observableList.add("Item " + Integer.toString(i) + ": " + itemName.get(i) + " Cost: " + itemCost.get(i));
                }
            }

            itemListView.refresh();
            itemListView.setItems(observableList);
        }
        catch(Exception e){
            System.out.println("Error searching");
        }
    }
    public void changeLocalUserDB(List<QueryDocumentSnapshot> documents){
        try {
            FileWriter writer = new FileWriter("Offline DB/Items_DB.txt", false);
            for(QueryDocumentSnapshot document: documents){
                String name = document.getString("name");
                Double cost = document.getDouble("cost");
                String id = document.getId();
                writer.write(name+"//"+cost+"//"+id);
                writer.write("\r\n");
            }
            writer.close();
            System.out.println("Set up offline DB");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void checkOfflineDatabase(){
        try {
            FileReader reader = new FileReader("Offline DB/Items_DB.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String name = line.split("//")[0];
                String cost = line.split("//")[1];
                String id = line.split("//")[2];
                System.out.println(name);
                System.out.println(cost);
                System.out.println(id);
                itemName.add(name);
                itemCost.add(Double.parseDouble(cost));
                itemID.add(id);
            }
            reader.close();
            System.out.println("Checked all items in database");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getting items from offline");
        }
    }


    public void createUnsyncedInvoice(){
        try {
            FileWriter writer = new FileWriter("Offline DB/Unsynced_Invoice.txt", true);
            writer.write(receptionistId+"//"+"0"+"//"+customerDocumentID+"//"+phoneNumber+"//"+totalBill.getText()+"//"+Long.toString(System.currentTimeMillis())+"//");
            writer.write(Integer.toString(boughtItemIDList.size())+"//");
            for(int i = 0; i<boughtItemIDList.size(); i++){
                writer.write(boughtItemIDList.get(i)+"//"+boughtItemQuantityList.get(i)+"//");
            }
            writer.write(paymentMethod);
            if (paymentMethod != "Cash"){
                writer.write("//"+refField.getText());
            }
            writer.write("\r\n");   // write new line
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAvailableItems(){

        try{
            if(!checkInternetConnection()){
                throw new Exception();
            }
            Firestore db = FirestoreClient.getFirestore();
            // asynchronously retrieve all items
            ApiFuture<QuerySnapshot> query = db.collection("products").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            changeLocalUserDB(documents);
            for (QueryDocumentSnapshot document : documents) {
                String name = document.getString("name");
                Double cost = document.getDouble("cost");

                itemName.add(name);
                itemCost.add(cost);
                itemID.add(document.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not fetch items from online database");
            System.out.println("Checking for items in Offline Database");
            checkOfflineDatabase();
        }
    }
    public void initialize() throws IOException {
        paymentMethod = "Cash";
        cashCheckBox.setSelected(true);

        itemName = new ArrayList<String>();
        itemCost = new ArrayList<Double>();
        itemID = new ArrayList<String>();
        costList = new ArrayList<Double>();
        boughtItemIDList = new ArrayList<String>();
        boughtItemNameList = new ArrayList<String>();
        boughtItemQuantityList = new ArrayList<Integer>();
        invoiceHelperRefPhone = new HashMap<String, String>();
        syncCustomer();

//        InputStream serviceAccount = new FileInputStream("robinhood-clinic-firebase-adminsdk-yzfpk-9aedc0fc60.json");
//        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
//        FirebaseApp.initializeApp(options);

        // Uncomment this (conserving api usage)
        setAvailableItems();

// Done: remove static items and use database later
//        itemName.add("X-Ray");
//        itemCost.add(200.0);
//        itemName.add("Cat Food");
//        itemCost.add(30.0);
//        itemName.add("Dog Collar");
//        itemCost.add(500.0);

        for(int i=0; i<itemName.size(); i++) {
            observableList.add("Item "+Integer.toString(i)+": "+itemName.get(i)+" Cost: "+itemCost.get(i));
        }
//        itemListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        for(int i=0; i<100; i++) {
//            observableList.add(new CustomCell("something"));
//        }
        itemListView.setItems(observableList);
        itemListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new CustomCell();
            }
        });

        System.out.println(itemListView);
        File file = new File("src/main/resources/com/example/robinhoodclinicpos/images/cameraOff.png");
        Image image = new Image(file.toURI().toString());

        webcamPhoto.setImage(image);
        //        Dimension resolution = new Dimension(1280, 720); // HD 720p

//        Dimension resolution = new Dimension(1920, 1080);// 1080p
        File prevPhoto = new File("src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg");
        deleteFile(prevPhoto);

    }
    public void deleteFile(File file){
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void startCamera(){
        Dimension resolution = new Dimension(480, 360);// 1080p
        webcam = Webcam.getDefault();
        webcam.setCustomViewSizes(new Dimension[] { resolution }); // register custom resolution
        webcam.setViewSize(resolution); // set it
        System.out.println("Webcam Initialized. Showing live view");
        webcam.open();
    }
    void startWebcamThread(){
        if (taskThread == null) {
            taskThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                webcamLiveView();
                            }
                        });
                        try {
                            sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
        taskThread.start();
    }
    boolean checkFileExists(String path){
        File f = new File(path);
        if (f.exists()){
            return true;
        }
        return false;
    }
    boolean syncLogin(){
        System.out.println("Syncing users Database");
        FileReader reader = null;
        try {
            reader = new FileReader("Offline DB/Unsynced_Login_Time.txt");

        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String userRef = line.split(" ")[0];
            String loginTime = line.split(" ")[1];
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference documentReference = db.collection("users").document(userRef);

            System.out.println("*** Found the document. Updating the data ***");
            ApiFuture<WriteResult> ft = documentReference.update("lastLogin", Long.parseLong(loginTime));
            WriteResult result = ft.get();
            System.out.println("Write result: " + result);
            System.out.println("Successfully Updated the login info");
        }
        reader.close();
        return true;
        } catch (Exception e) {
            return false;
        }

    }
    boolean syncCustomer(){
        System.out.println("Syncing Customers Database");
        FileReader reader = null;
//        invoiceHelperPhoneNumber = new ArrayList<String>();
//        invoiceHelperCustomerRef = new ArrayList<String>();
        try {
            reader = new FileReader("Offline DB/Unsynced_Customer.txt");

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            Firestore db = FirestoreClient.getFirestore();

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue;
                String customerName = line.split("//")[0];
                String customerPhone = line.split("//")[1];
                String customerAddress = line.split("//")[2];
                Long registeredOn = Long.parseLong(line.split("//")[3]);
                String customerPhotoPath = line.split("//")[4];
                System.out.println(customerName);
                System.out.println(customerPhone);
                System.out.println(customerAddress);
                System.out.println(registeredOn);
                System.out.println(customerPhotoPath);

                //****************

                String firebasePhotoPath = "CUSTOMER_PHOTOS_FOLDER/" + customerPhone + ".jpg";
                String myPhotoPath = customerPhotoPath;
                Map<String, Object> data = new HashMap<>();
                data.put("name", customerName);
                data.put("address", customerAddress);
                data.put("phone", registeredOn);
                data.put("registered", registeredOn);
                data.put("photoPath", firebasePhotoPath);
                ApiFuture<DocumentReference> ref = db.collection("customers").add(data);

                //store the phonenumber and reference for further use
//                invoiceHelperCustomerRef.add(ref.get().getId());
//                invoiceHelperPhoneNumber.add(customerPhone);
                invoiceHelperRefPhone.put(ref.get().getId(), customerPhone);
                System.out.println("Testing 2");
                StorageClient storageClient = StorageClient.getInstance();
                InputStream photo = new FileInputStream(myPhotoPath);

                storageClient.bucket().create(firebasePhotoPath, photo, Bucket.BlobWriteOption.userProject("robinhood-clinic"));
                Path path = FileSystems.getDefault().getPath(myPhotoPath);
                try {
                    Files.delete(path);
                } catch (Exception x) {
                    System.out.println("Error Deleting the file");
                }
                System.out.println("Successfully added a customer to online DB");
                System.out.println(ref.get().getId());
            }
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    boolean syncInvoice(){
        System.out.println("Syncing Invoice Database");

        FileReader reader = null;
        try {
            reader = new FileReader("Offline DB/Unsynced_Invoice.txt");

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
//            writer.write(receptionistId+"//"+"0"+"//"+customerDocumentID+"//"+phoneNumber+"//"+totalBill.getText()+"//"+Long.toString(System.currentTimeMillis())+"//");
//            writer.write(Integer.toString(boughtItemIDList.size())+"\\");
//            for(int i = 0; i<boughtItemIDList.size(); i++){
//                writer.write(boughtItemIDList.get(i)+"//"+boughtItemQuantityList.get(i)+"//");
//            }
//            writer.write(paymentMethod);
//            if (paymentMethod != "Cash"){
//                writer.write("//"+refField.getText());
//            }
            Firestore db = FirestoreClient.getFirestore();

            while ((line = bufferedReader.readLine()) != null) {
                for(String i: line.split("//")){
                    System.out.println(i);
                }
                String receptionistId = line.split("//")[0];
                double discount = Double.parseDouble(line.split("//")[1]);
                String customerDocumentID = line.split("//")[2];
                String phoneNumber = line.split("//")[3];
                double total = Double.parseDouble(line.split("//")[4]);
                long time = Long.parseLong(line.split("//")[5]);
                int itemCount = Integer.parseInt(line.split("//")[6]);
                if(invoiceHelperRefPhone.containsKey(phoneNumber)){
                    //Customer is was saved while offline
                    //So we don't need to search the database for reference again.
                    customerDocumentID = invoiceHelperRefPhone.get(phoneNumber);
                }else{
                    //We will search for the reference in the database.
                    //*******Find customer with phone number
                    try {
                        CollectionReference customers = db.collection("customers");

                        Query query = customers.whereEqualTo("phone", phoneNumber);
                        ApiFuture<QuerySnapshot> querySnapshot = query.get();
                        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                            customerDocumentID = document.getId();
                            break;
                        }

                    } catch (Exception e) {
//                        System.out.println(e);
                        System.out.println("Error Retrieving old Customer!");
                    }
                    //**************************
                }
                //If for some ridiculous reason everything fails, we can still check push the invoice
                //based on the phone number

                System.out.println(receptionistId);
                System.out.println(discount);
                System.out.println(customerDocumentID);
                System.out.println(phoneNumber);
                System.out.println(total);
                System.out.println(time);
                System.out.println(itemCount);
                Map<String, Integer> items = new HashMap<String, Integer>();
                for(int i = 0; i<itemCount; i++){
                    items.put(line.split("//")[2*i+7], Integer.parseInt(line.split("//")[2*i+7+1]));
                    System.out.println(line.split("//")[2*i+7]);
                    System.out.println(Integer.parseInt(line.split("//")[2*i+7+1]));
                }
                int currIndex = itemCount*2+7;
                String pm = line.split("//")[currIndex];
                System.out.println(pm);

                Map<String, Object> data = new HashMap<>();
                data.put("invoiceBy", db.collection("users").document(receptionistId));
                data.put("discount", 0);
                data.put("items", items);
                data.put("customerId", db.collection("customers").document(customerDocumentID));
                data.put("paymentMethod", pm);
                if (pm != "Cash"){
                    if(pm == "Other"){
                        data.put("Other", line.split("//")[currIndex+1]);
                    }
                    else{
                        data.put("paymentRef", line.split("//")[currIndex+1]);
                    }
                }
                data.put("totalBill", total);
                data.put("time", time);
                ApiFuture<DocumentReference> ref = db.collection("invoices").add(data);
                String invoiceId = ref.get().getId();
                System.out.println("Successfully Added an invoice to online DB");
                System.out.println(invoiceId);

            }
            reader.close();
            return true;
        } catch (Exception e) {
            System.out.println("*********Error saving invoice ONLINE DB**********");
            e.printStackTrace();
            return false;
        }

    }
    boolean checkInternetConnection(){
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println("Internet is connected");
            return true;
        } catch (Exception e) {
            System.out.println("Internet is not connected");
            return false;
        }
    }
    void syncDBFiles(){
        if(!checkInternetConnection()){
           return;
        }
        System.out.println("Syncing all db files");
        //check if any Unsynced files exist or not
        if (checkFileExists("Offline DB/Unsynced_Login_Time.txt")){
            //Sync the login time
            if(syncLogin()){
                System.out.println("Marked Unsynced_login_time.txt for deletion");
                deleteFile(new File("Offline DB/Unsynced_Login_Time.txt"));

            }
            else{
                return;
            }
        }
        if (checkFileExists("Offline DB/Unsynced_Customer.txt")){
            //Add the customer unconditionally for now
            if(syncCustomer()){
                System.out.println("Marked Unsynced_Customer.txt for deletion");
                deleteFile(new File("Offline DB/Unsynced_Customer.txt"));

            }
            else{
                return;
            }
        }
        if (checkFileExists("Offline DB/Unsynced_Invoice.txt")){
            //Add the customer unconditionally for now
            if(syncInvoice()){                System.out.println("Marked Offline DB/Unsynced_Invoice.txt for deletion");
                deleteFile(new File("Offline DB/Unsynced_Invoice.txt"));

            }
            else{
                return;
            }
        }
        syncThread.interrupt();
        syncThread.stop();
        syncThread = null;

    }
    void startSyncThread(){
        if (syncThread == null) {
            syncThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                syncDBFiles();
                                if (syncThread==null) return;
                            }
                        });
                        try {
                            sleep(30000);
                        } catch (InterruptedException e) {
                            System.out.println("Could not sleep the syncThread");
                            System.out.println("Exit");
                        }

                    }
                }
            });
        }
        syncThread.start();
    }

}
