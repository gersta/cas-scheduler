package de.gerritstapper.casscheduler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.services.DataProcessService;
import de.gerritstapper.casscheduler.services.LectureService;
import de.gerritstapper.casscheduler.services.PdfReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements ApplicationRunner {

    private LectureService lectureService;

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
        List<Lecture> lectures = service.readPdf(0);
        List<LectureDao> daos = lectures.stream()
                .map(lecture -> DataProcessService.map(lecture))
                .peek(dao -> System.out.println(dao))
                .collect(Collectors.toList());

        lectureService.saveAll(daos);
    }
}
