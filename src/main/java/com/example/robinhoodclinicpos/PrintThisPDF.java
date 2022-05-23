package com.example.robinhoodclinicpos;


import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;

public class PrintThisPDF {
    PrintThisPDF(String fileLocation){
        try {
            FileInputStream fis = new FileInputStream(fileLocation);
            Doc pdfDoc = new SimpleDoc(fis, null, null);
            PrinterJob job = PrinterJob.getPrinterJob();
            PrintService printService = job.getPrintService();
            DocPrintJob printJob = printService.createPrintJob();

            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());

            fis.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not print PDF");
        }
    }
}
