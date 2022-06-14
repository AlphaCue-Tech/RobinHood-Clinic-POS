package com.example.robinhoodclinicpos;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.util.ArrayList;

public class DailySummaryPdfGenerator {


    public static double sumAllCost(ArrayList<Double> l){
        double sum = 0f;
        for(int i = 0; i<l.size(); i++){
            sum += l.get(i);
        }
        return sum;
    }
    DailySummaryPdfGenerator(ArrayList<String> usernameList,ArrayList<String> phoneNumberList, ArrayList<Double> costList, ArrayList<Double> additionalCostList, ArrayList<String> costDescriptionList){
//    public static void gen(ArrayList<String> usernameList, ArrayList<String> phoneNumberList, ArrayList<Double> costList, ArrayList<Double> additionalCostList, ArrayList<String> costDescriptionList){
        try{
            String imgPath = "./src/main/resources/com/example/robinhoodclinicpos/images/RobinHoodWatermark.png";
            float newWatermarkImageWidth = 300f;
            float newWatermarkImageHeight = 300f;
            String headingImage1 = "./src/main/resources/com/example/robinhoodclinicpos/images/RobinHoodHead1.png";
            String headingImage2 = "./src/main/resources/com/example/robinhoodclinicpos/images/RobinHoodHead2.png";
            String QRImagePath = "./src/main/resources/com/example/robinhoodclinicpos/images/QR.jpg";
            String iconImagePath = "./src/main/resources/com/example/robinhoodclinicpos/images/robinhoodicon.png";
            System.out.println("Succesfull");
            String pdfPath= "./PDF/dailySummaryPDF.pdf";
            PdfWriter pdfWriter = new PdfWriter(pdfPath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument, PageSize.A4);
            PdfPage pdfPage = pdfDocument.addNewPage();
            PdfCanvas pdfCanvas = new PdfCanvas(pdfPage);

            ImageData imageData = ImageDataFactory.create(imgPath);
            Image image = new Image(imageData);
            image.setWidth(newWatermarkImageWidth);
            image.setHeight(newWatermarkImageHeight);
            image.setFixedPosition((pdfDocument.getDefaultPageSize().getWidth()-newWatermarkImageWidth)/2, (pdfDocument.getDefaultPageSize().getHeight()-100-newWatermarkImageHeight)/2);
            image.setOpacity(0.3f);
            document.add(image);

            image = new Image(ImageDataFactory.create(iconImagePath));
            image.setFixedPosition(60, 720);
            System.out.println(pdfDocument.getDefaultPageSize().getWidth());
            System.out.println(pdfDocument.getDefaultPageSize().getHeight());
            image.setWidth(80);
            image.setHeight(80);
            document.add(image);





            //Spacing between elements
//        document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 2f));
            Image head1 = new Image(ImageDataFactory.create(headingImage1));
            head1.setHorizontalAlignment(HorizontalAlignment.CENTER);
            head1.setMarginLeft(40f);
            head1.setHeight(50f);
            document.add(head1.setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Call for Animal Rescue-999, Animal Rescue").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Daily Summary").setFontSize(15).setTextAlignment(TextAlignment.CENTER));

            //        document.add(new Paragraph("Animal Care Trust Bangladesh").setTextAlignment(TextAlignment.CENTER));
//        document.add(new Paragraph("RobinHood Care - Animal Clinic").setCharacterSpacing(1.2f).setTextAlignment(TextAlignment.CENTER));
//            document.add(new Paragraph("Hotline: +8801616737283/999, House: 195/1, Road-9 Block-a Tilpapara Khilgaon Dhaka-1219.").setTextAlignment(TextAlignment.CENTER).setFontSize(10f));

            SolidLine line = new SolidLine(0.7f);
            line.setColor(ColorConstants.BLACK);
            LineSeparator ls = new LineSeparator(line);
            ls.setWidth(520);
            ls.setMarginTop(5);
            document.add(ls);

            document.add(new Paragraph("\u00A0\u00A0\u00A0\u00A0\u00A0"));
            document.add(new Paragraph("Customers").setFontSize(14).setBold().setCharacterSpacing(1.2f).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\u00A0\u00A0\u00A0"));
            float threecol=190f;
            float twocol=286f;
            float twocol150= twocol+150f;
            float twocolumnWidth[]={twocol150, twocol};
            float fullWidth[]={threecol*3};
            float eqcol = 590f/2;
            double profit = sumAllCost(costList);
            double additionalCost = sumAllCost(additionalCostList);
            System.out.println("Sizes");
            System.out.println(usernameList.size());

            System.out.println(costList.size());

            System.out.println(phoneNumberList.size());
            System.out.println(additionalCostList.size());
            System.out.println(costDescriptionList.size());

            System.out.println("Sizes");
            System.out.println("Sizes");

            Table customerTable = new Table(new float[]{threecol, threecol, threecol});
            customerTable.addHeaderCell(new Cell().add(new Paragraph("Name").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            customerTable.addHeaderCell(new Cell().add(new Paragraph("Phone").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            customerTable.addHeaderCell(new Cell().add(new Paragraph("Price (BDT)").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            for (int i = 0; i<usernameList.size(); i++){
                customerTable.addCell(new Cell().add(new Paragraph(usernameList.get(i)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
                customerTable.addCell(new Cell().add(new Paragraph(phoneNumberList.get(i)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
                customerTable.addCell(new Cell().add(new Paragraph(Double.toString(costList.get(i))).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
            }
            customerTable.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
            customerTable.addCell(new Cell().add(new Paragraph("Total (BDT) :").setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
            customerTable.addCell(new Cell().add(new Paragraph(Double.toString(profit)).setTextAlignment(TextAlignment.CENTER)).setBold().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));

            document.add(customerTable);
            System.out.println("customer correctly added");
            document.add(ls);

            document.add(new Paragraph("\u00A0\u00A0\u00A0\u00A0\u00A0"));
            document.add(new Paragraph("Costs").setFontSize(14).setBold().setCharacterSpacing(1.2f).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\u00A0\u00A0\u00A0"));
            Table additionalCostsTable = new Table(new float[]{eqcol, eqcol});
            additionalCostsTable.addHeaderCell(new Cell().add(new Paragraph("Cost Description").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            additionalCostsTable.addHeaderCell(new Cell().add(new Paragraph("Cost").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            for (int i = 0; i<additionalCostList.size(); i++){
                additionalCostsTable.addCell(new Cell().add(new Paragraph(costDescriptionList.get(i)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
                additionalCostsTable.addCell(new Cell().add(new Paragraph(Double.toString(additionalCostList.get(i))).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
            }
            additionalCostsTable.addCell(new Cell().add(new Paragraph("Total Expenses:").setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
            additionalCostsTable.addCell(new Cell().add(new Paragraph(Double.toString(additionalCost)).setTextAlignment(TextAlignment.CENTER)).setBold().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));

            document.add(additionalCostsTable);
            document.add(ls);
            document.add(new Paragraph("\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"));
            Table finalCostsTable = new Table(new float[]{eqcol, eqcol});
            finalCostsTable.addHeaderCell(new Cell().add(new Paragraph("Profit: ").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            finalCostsTable.addHeaderCell(new Cell().add(new Paragraph(Double.toString(profit-additionalCost)).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
            document.add(finalCostsTable);

//            String name = "Someone";
//            String phone = "11111111111";
//            String addr = "11/23 washington street, borishal";
//            String paymentMethod = "bKash";
//            Date d = Calendar.getInstance().getTime();
//            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
//            String date = dateFormat.format(d);
//        String date = "20 Dec 2023";
//            String refNo = "f-123123";
//
//            infCol1.addCell(new Cell().add(new Paragraph("Name:")).setBold().setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph(fullName)).setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph("Phone:")).setBold().setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph(phoneNumber)).setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph("Address")).setBold().setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph(address)).setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph("Payment Method:")).setBold().setBorder(Border.NO_BORDER));
//            infCol1.addCell(new Cell().add(new Paragraph(paymentMethod)).setBorder(Border.NO_BORDER));
//            inf.addCell(new Cell().add(infCol1).setBorder(Border.NO_BORDER));
//
//            infCol2.addCell(new Cell().add(new Paragraph("Date:")).setBold().setBorder(Border.NO_BORDER));
//            infCol2.addCell(new Cell().add(new Paragraph(date)).setBorder(Border.NO_BORDER));
//            if (!paymentMethod.equals("Cash")){
//                if(!paymentMethod.equals("Other")) {
//                    infCol2.addCell(new Cell().add(new Paragraph("Ref. No:")).setBold().setBorder(Border.NO_BORDER));
//                }
//                else{
//                    infCol2.addCell(new Cell().add(new Paragraph("Other:")).setBold().setBorder(Border.NO_BORDER));
//                }
//                infCol2.addCell(new Cell().add(new Paragraph(payRef)).setBorder(Border.NO_BORDER));
//            }
//            inf.addCell(new Cell().add(infCol2).setBorder(Border.NO_BORDER));
//
//            document.add(inf);
//
//            document.add(ls);
//
//            Paragraph p2 = new Paragraph("INVOICE").setFontSize(15f).setCharacterSpacing(2.5f).setBold().setTextAlignment(TextAlignment.CENTER);
//            document.add(p2.setFontSize(15f));
//
////            ArrayList<String> itemName = new ArrayList<String>();
////            itemName.add("X-ray");
////            itemName.add("Dog Collar");
////            itemName.add("Cat Food");
////            ArrayList<Double> itemCost = new ArrayList<Double>();
////            itemCost.add(200.0);
////            itemCost.add(500.0);
////            itemCost.add(30.0);
////            ArrayList<Integer> itemQuantity = new ArrayList<Integer>();
////            itemQuantity.add(1);
////            itemQuantity.add(2);
////            itemQuantity.add(3);
//
//
//
//            Table priceTable = new Table(new float[]{threecol, threecol, threecol});
//            priceTable.addHeaderCell(new Cell().add(new Paragraph("Item Name").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
//            priceTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
//            priceTable.addHeaderCell(new Cell().add(new Paragraph("Price (BDT)").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.BLACK, 0.9f).setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
//
//            for (int i = 0; i<boughtItemNameList.size(); i++){
//                priceTable.addCell(new Cell().add(new Paragraph(boughtItemNameList.get(i)).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//                priceTable.addCell(new Cell().add(new Paragraph(Integer.toString(boughtItemQuantityList.get(i))).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//                priceTable.addCell(new Cell().add(new Paragraph(Double.toString(costList.get(i))).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//
//            }
//
////        priceTable.addCell(new Cell().add(new Paragraph("Hand Sanitizer").setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("2").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("40").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("Iphone 13").setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("1").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("170000").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("GPU").setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("2").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
////        priceTable.addCell(new Cell().add(new Paragraph("80000").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
////
//            priceTable.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//            priceTable.addCell(new Cell().add(new Paragraph("Total (BDT) :").setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//            priceTable.addCell(new Cell().add(new Paragraph(totalBill).setTextAlignment(TextAlignment.CENTER)).setBold().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)));
//            document.add(priceTable);

//            Paragraph footer1 = new Paragraph("Call-999 and ask for help from RobinHood").setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
//            Paragraph footer2 = new Paragraph("We are open 7 days").setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
//
//            footer1.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            Rectangle rectangle1 = new Rectangle(15, 0, 600, 50);
//            Canvas canvas1 = new Canvas(pdfCanvas, rectangle1);
//            canvas1.add(footer1);
//            canvas1.close();
//
//            footer2.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            Rectangle rectangle2 = new Rectangle(15, 0, 600, 35);
//            Canvas canvas2 = new Canvas(pdfCanvas, rectangle2);
//            canvas2.add(footer2);
////            image = new Image(ImageDataFactory.create(QRImagePath));
//////        image.setFixedPosition(40, 30);
////            image.setFixedPosition(460, 700);
////
////            image.setWidth(80);
////            image.setHeight(80);
////            document.add(image);
//            canvas1.close();
//            canvas2.close();
//            pdfCanvas.saveState()
//                    .setLineWidth(0.7f)
//                    .setStrokeColor(ColorConstants.BLACK)
//                    .moveTo(40, 60 )
//                    .lineTo(550, 60)
//                    .stroke()
//                    .restoreState();

//        Table footer = new Table(new float[]{590f});
//        footer.setExtendBottomRow(true);
//        footer.addCell(new Cell().add(new Paragraph("Come Join Us").setTextAlignment(TextAlignment.CENTER)).setBorderTop(new SolidBorder(ColorConstants.GRAY, 0.3f)).setVerticalAlignment(VerticalAlignment.BOTTOM));
//        document.add(footer);
            document.close();
            new PrintThisPDF(pdfPath);

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Could not generate Daily Summary PDF");
        }

    }
//    public static void main(String[] args){
//        System.out.println("This runs");
//        ArrayList<String> usernameList = new ArrayList<String>();
//        ArrayList<String> phoneNumberList = new ArrayList<String>();
//        ArrayList<Double> costList = new ArrayList<Double>();
//
//        ArrayList<String> costDescriptionList = new ArrayList<String>();
//        ArrayList<Double> additionalCostList = new ArrayList<Double>();
//
//        for (int i = 0; i<100; i++){
//            usernameList.add("JIAN");
//            phoneNumberList.add("0101010101010");
//            costList.add(200.0);
//        }
//        for (int i = 0; i<10; i++){
//            costDescriptionList.add("POOOLICIA");
//            additionalCostList.add(200.0);
//        }
//
//
//        gen(usernameList,phoneNumberList,costList,additionalCostList,costDescriptionList);
//    }

}
