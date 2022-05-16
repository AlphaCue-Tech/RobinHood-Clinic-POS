package com.example.robinhoodclinicpos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

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

    private ObservableList<String> observableList = FXCollections.observableArrayList();

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
