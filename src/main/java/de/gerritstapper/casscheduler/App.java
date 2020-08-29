package de.gerritstapper.casscheduler;

import java.io.IOException;

import de.gerritstapper.casscheduler.services.PdfReaderService;

public class App {

    public static void main(String[] args) throws IOException {
        PdfReaderService service = new PdfReaderService("M_T_Lehrveranstaltungen.pdf");
        service.readPdf(0);
    }

}
