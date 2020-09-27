package de.gerritstapper.casscheduler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.services.*;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements ApplicationRunner {

    private LectureService lectureService;

    private static final String OUTPUT_DIR = "lectures/";

    @Autowired
    public App(LectureService service, ApplicationContext context) {
        this.lectureService = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws IOException {
        PdfReaderService service = new PdfReaderService("M_T_Lehrveranstaltungen.pdf");
        List<Lecture> lectures = service.readPdf(null);

        List<Calendar> calendars = lectures.stream()
                .map(lecture -> DataProcessService.create(lecture))
                .map(dao -> IcsCreatorService.create(dao))
                .map(cal -> IcsSaverService.saveFile(cal, OUTPUT_DIR))
                .collect(Collectors.toList());


        // lectureService.saveAll(daos);
    }
}
