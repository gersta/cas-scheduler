package de.gerritstapper.casscheduler;

import java.io.IOException;

import de.gerritstapper.casscheduler.services.PdfReaderService;
import de.gerritstapper.casscheduler.util.OffsetUtil;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        PdfReaderService service = new PdfReaderService("M_T_Lehrveranstaltungen.pdf");
        service.readPdf(null).forEach(lecture -> System.out.println(lecture));
    }

}
