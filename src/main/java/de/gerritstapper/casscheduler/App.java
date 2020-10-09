package de.gerritstapper.casscheduler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.services.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.services.ics.IcsSaverService;
import de.gerritstapper.casscheduler.services.persistence.DataProcessService;
import de.gerritstapper.casscheduler.services.persistence.ILectureDaoPersistenceService;
import de.gerritstapper.casscheduler.services.persistence.SqlLectureDaoPersistenceService;
import de.gerritstapper.casscheduler.services.pdf.PdfReaderService;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements ApplicationRunner {

    private ILectureDaoPersistenceService lectureDaoPersistenceService;

    private static final String OUTPUT_DIR = "lectures/";

    @Autowired
    public App(ILectureDaoPersistenceService lectureDaoPersistenceService, ApplicationContext context) {
        this.lectureDaoPersistenceService = lectureDaoPersistenceService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws IOException {
        PdfReaderService service = new PdfReaderService("M_T_Lehrveranstaltungen.pdf");
        List<Lecture> lectures = service.readPdf(null);

        System.out.println("Lecture size: " +  lectures.size());

        List<List<LectureDao>> daoLists = lectures.stream()
                                                    .map(lecture -> DataProcessService.create(lecture))
                                                    .collect(Collectors.toList());

        List<LectureDao> daos = daoLists.stream()
                                        .flatMap(daoList -> daoList.stream())
                                        .collect(Collectors.toList());

        System.out.println("DAO size: " + daos.size());
        lectureDaoPersistenceService.saveAll(daos);

        List<Calendar> calendars = daos.stream()
                                        .map(dao -> IcsCreatorService.create(dao))
                                        .map(cal -> IcsSaverService.saveFile(cal, OUTPUT_DIR))
                                        .collect(Collectors.toList());

        System.out.println("Calenders size: " + calendars.size());
    }
}
