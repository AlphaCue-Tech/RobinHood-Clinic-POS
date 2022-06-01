package com.example.robinhoodclinicpos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdditionalCostController {
    ArrayList<String> usernameList;
    ArrayList<String> phoneNumberList;
    ArrayList<Double> costList;
    @FXML
    private TextField costDescription;
    @FXML
    private TextField cost;

    private ArrayList<String> costDescriptionList;
    private ArrayList<Double> additionalCostList;

    private String receptionistId;

    public long getTodayTimeInMillis(){
        long t = System.currentTimeMillis();
//        String ts = "1654094199744";
//        Date d3 = new Date(Long.parseLong(ts));
//        System.out.println(d3);
//        System.out.println(t);
        Date d = new Date(t);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String date = dateFormat.format(d);
        Date d2 = new Date(date);
//        Date d2 = null;
//        try {
//            d2 = DateFormat.getInstance().parse(date);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(d2);
//        System.out.println(d2.getTime());
//        String date = "20 Dec 2023";

//        System.out.println(d.getTime());
//        System.out.println(d);
//        System.out.println(d.getDate());
//        System.out.println(d.toString());
//        Date d = Calendar.getInstance().getTime();
        return d2.getTime();
    }
    public void setReceptionistId(String id){
        receptionistId = id;
    }
    public void getInvoicesForToday(){
        Firestore db = FirestoreClient.getFirestore();
        // asynchronously retrieve all items
        ApiFuture<QuerySnapshot> query = db.collection("invoices").get();

        QuerySnapshot querySnapshot = null;
        usernameList = new ArrayList<String>();
        phoneNumberList = new ArrayList<String>();
        costList = new ArrayList<Double>();

        try{
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
//                String name = document.getString("name");
//                Double cost = document.getDouble("cost");
                System.out.println("Ends here");
                DocumentReference recepRef = (DocumentReference) document.get("invoiceBy");
                String[] st = recepRef.toString().split("/");
                int len = st.length;
//                System.out.println(st[len-1]);
                String recepId = st[len-1].split("}")[0];
                System.out.println(recepId);
//
//                ApiFuture<DocumentSnapshot> q1 = recep.get();
//                DocumentSnapshot q = q1.get();
//                q.getId();
//                System.out.println(customer);
//
//                String rec = document.getString("invoiceBy");
//                System.out.println(rec);
//                //if because it references something else, I have to print it and then
                //check what receptionistId I am acutally getting

                if (recepId.equals(receptionistId)){
                    //Check if the invoice was generated in the same day
//                    String timeString = document.getString("time");
                    long time = document.getLong("time");
//                    long time = Long.parseLong(timeString);
                    long today = getTodayTimeInMillis();
                    if (time>today){
//                        System.out.println(document.getString("customerId"));
                        DocumentReference customerRef = (DocumentReference) document.get("customerId");

                        ApiFuture<DocumentSnapshot> q1 = customerRef.get();
                        DocumentSnapshot customer = q1.get();
                        String uname = customer.getString("name");
                        String phone = customer.getString("phone");
                        Double totalBill = customer.getDouble("totalBill");
                        Double discount = customer.getDouble("discount");
                        System.out.println(uname);
                        System.out.println(phone);
                        System.out.println(totalBill);
                        System.out.println(discount);
                        usernameList.add(uname);
                        phoneNumberList.add(phone);
                        costList.add(totalBill-discount);
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not fetch documents");
        }
    }
    @FXML
    protected void addCostButtonPressed(){
        additionalCostList.add(Double.parseDouble(cost.getText()));
        costDescriptionList.add(costDescription.getText());
        cost.setText("");
        costDescription.setText("");
    }
    @FXML
    protected void doneButtonPressed(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Finished for Today?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
//            for(int i = 0; i<costList.size(); i++){
//                System.out.println(costList.get(i));
//                System.out.println(costDescriptionList.get(i));
//            }
            //Get all invoices of today from database and generate pdf
            getInvoicesForToday();
            //Generate summary PDF
            new DailySummaryPdfGenerator(usernameList, phoneNumberList, costList, additionalCostList, costDescriptionList);
            //Print PDF
            Stage stage = (Stage) cost.getScene().getWindow();
            stage.close();
        }
    }
    public void initialize() {
        costDescriptionList = new ArrayList<String>();
        costList = new ArrayList<Double>();
    }

}
