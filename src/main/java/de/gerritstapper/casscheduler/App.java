package de.gerritstapper.casscheduler;

import java.io.IOException;

import de.gerritstapper.casscheduler.services.PdfReaderService;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        PdfReaderService service = new PdfReaderService();
        service.readPdf("M_T_Lehrveranstaltungen.pdf");
    }

}
