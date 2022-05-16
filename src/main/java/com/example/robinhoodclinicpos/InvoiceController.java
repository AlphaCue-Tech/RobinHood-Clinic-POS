package com.example.robinhoodclinicpos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.bytedeco.javacv.*;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;

import java.awt.event.WindowEvent;

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
public class InvoiceController {
    @FXML
    private ListView<String> itemListView;

    @FXML
    private ImageView webcamPhoto;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML
    protected void onTakePhotoButtonPressed(){
        try {
            FrameGrabber grabber = new OpenCVFrameGrabber(0);
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            grabber.start();
            Frame frame = grabber.grab();
            IplImage img = converter.convert(frame);
            cvSaveImage("src/main/resources/com/example/robinhoodclinicpos/images/selfie.png", img);
            Thread.sleep(5000);
            webcamPhoto.setImage(new Image(getClass().getResourceAsStream("images/selfie.png")));
        } catch(Exception e){
            System.out.println(e);
            System.out.println("Encountered an error while taking webcam photo");
        }
//            FrameGrabber grabber;
    }
    public void initialize(){
        for(int i=0; i<100; i++) {
            observableList.add("Item no. "+Integer.toString(i));
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
