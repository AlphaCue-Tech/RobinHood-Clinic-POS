package com.example.robinhoodclinicpos;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private ObservableList<String> customerItemObservableList = FXCollections.observableArrayList();
    private ArrayList<Double> costList;
    private ArrayList<String> itemName;
    private ArrayList<Double> itemCost;

    private String fullName;
    private String phoneNumber;
    private String address;
    private Stage addCustomerStage;
    public String getFullName(){
        return fullName;
    }
    @FXML protected void cashChecked(){
        if (cashCheckBox.isSelected()){
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }

    }
    @FXML
    protected void bKashChecked(){
        if (bKashCheckBox.isSelected()){
            cashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }

    }
    @FXML
    protected void nogodChecked(){
        if (nogodCheckBox.isSelected()){
            bKashCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }
    }
    @FXML
    protected void cardChecked(){
        if (cardCheckBox.isSelected()){
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
            otherCheckBox.setSelected(false);
        }

    }
    @FXML
    protected void otherChecked(){
        if (otherCheckBox.isSelected()){
            bKashCheckBox.setSelected(false);
            nogodCheckBox.setSelected(false);
            cardCheckBox.setSelected(false);
            cashCheckBox.setSelected(false);
        }
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
    protected void generateInvoicePressed(){
        System.out.println();
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
    protected void onTakePhotoButtonPressed(){
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();

        try {
            ImageIO.write(image, ImageUtils.FORMAT_JPG, new File("src/main/resources/com/example/robinhoodclinicpos/images/selfie.jpg"));
            Thread.sleep(3000);
            Image im = SwingFXUtils.toFXImage(image, null);
            webcamPhoto.setImage(im);
        } catch (Exception e) {

            System.out.println("Could not capture image");
            System.out.println(e);
        }
        finally {
            //not closing the webcam makes for faster image capturing
//            webcam.close();
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
    public void initialize() throws IOException {
        itemName = new ArrayList<String>();
        itemCost = new ArrayList<Double>();
        costList = new ArrayList<Double>();

//        InputStream serviceAccount = new FileInputStream("robinhood-clinic-firebase-adminsdk-yzfpk-9aedc0fc60.json");
//        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
//        FirebaseApp.initializeApp(options);
        Firestore db = FirestoreClient.getFirestore();
        // asynchronously retrieve all users

        // TODO: Uncomment this (conserving api usage)
        ApiFuture<QuerySnapshot> query = db.collection("products").get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (Exception e) {
           System.out.println("Error fetching data");
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String name = document.getString("name");
            Double cost = document.getDouble("cost");
            itemName.add(name);
            itemCost.add(cost);
//            System.out.println("User: " + document.getId());
//            System.out.println("First: " + document.getString("name"));
//            System.out.println("Middle: " + document.getDouble("cost"));
        }

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
    }


}
