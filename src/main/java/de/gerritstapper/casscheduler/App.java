package de.gerritstapper.casscheduler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.models.DatesTuple;
import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.models.LectureDao;
import de.gerritstapper.casscheduler.services.DataProcessService;
import de.gerritstapper.casscheduler.services.PdfReaderService;

public class App {

    public static void main(String[] args) throws IOException {
        PdfReaderService service = new PdfReaderService("M_T_Lehrveranstaltungen.pdf");
        List<Lecture> lectures = service.readPdf(0);
        List<LectureDao> daos = lectures.stream()
                                        .map(lecture -> DataProcessService.map(lecture))
                                        .peek(dao -> System.out.println(dao))
                                        .collect(Collectors.toList());
    }
}
