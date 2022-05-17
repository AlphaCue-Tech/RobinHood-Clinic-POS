package com.example.robinhoodclinicpos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bytedeco.javacv.*;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.w3c.dom.Text;

import javax.swing.*;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;
//import org.bytedeco.javacv.*;

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
    private Label totalBill;
    @FXML
    private ImageView webcamPhoto;
    @FXML
    private TextField searchTextField;
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private ObservableList<String> customerItemObservableList = FXCollections.observableArrayList();
    private ArrayList<Double> costList;
    private ArrayList<String> itemName;
    private ArrayList<Double> itemCost;
    private Stage addCustomerStage;

    @FXML
    protected void addCustomerButtonPressed(){
        System.out.println("Adding a new customer");
        try {
            FXMLLoader loader = new FXMLLoader(RobinHoodApplication.class.getResource("addCustomer-view.fxml"));

            addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(loader.load(), 800, 600));
            addCustomerStage.show();
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
        try {
            FrameGrabber grabber = new OpenCVFrameGrabber(0);
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            grabber.start();
            Frame frame = grabber.grab();
            IplImage img = converter.convert(frame);
//            cvSaveImage("src/main/resources/com/example/robinhoodclinicpos/images/selfie.png", img);

            cvSaveImage("selfie.png", img);
            Thread.sleep(3000);
//            webcamPhoto.setImage(new Image(getClass().getResourceAsStream("images/selfie.png")));

            webcamPhoto.setImage(new Image(getClass().getResourceAsStream("../../../../selfie.png")));
        } catch(Exception e){
            System.out.println(e);
            System.out.println("Encountered an error while taking webcam photo");
        }
//            FrameGrabber grabber;
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
    public void initialize(){
        itemName = new ArrayList<String>();
        itemCost = new ArrayList<Double>();
        costList = new ArrayList<Double>();
        itemName.add("X-Ray");
        itemCost.add(200.0);
        itemName.add("Cat Food");
        itemCost.add(30.0);
        itemName.add("Dog Collar");
        itemCost.add(500.0);

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
