package com.example.robinhoodclinicpos;


import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PrintThisPDF {
    PrintThisPDF(String fileLocation, String PrinterName){
        try {
            PDDocument document = PDDocument.load(new File(fileLocation));

            PrintService myPrintService = findPrintService(PrinterName); //HP LaserJet Pro M12a
            System.out.println(myPrintService);

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.setPrintService(myPrintService);
            job.print();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Printer Error. Could not print PDF");
        }
    }

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

}
