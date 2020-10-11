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
import de.gerritstapper.casscheduler.services.pdf.PdfReaderService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements ApplicationRunner {

    private final PdfReaderService pdfService;
    private final DataProcessService dataProcessService;
    private final ILectureDaoPersistenceService lectureDaoPersistenceService;
    private final IcsCreatorService icsCreatorService;
    private final IcsSaverService icsSaverService;
    private final JsonFileUtil jsonFileUtil;

    private static final String OUTPUT_DIR = "lectures/";

    @Autowired
    public App(
            final PdfReaderService pdfService,
            final DataProcessService dataProcessService,
            final ILectureDaoPersistenceService lectureDaoPersistenceService,
            final IcsCreatorService icsCreatorService,
            final IcsSaverService icsSaverService,
            final JsonFileUtil jsonFileUtil,
            ApplicationContext context) {
        this.pdfService = pdfService;
        this.dataProcessService = dataProcessService;
        this.lectureDaoPersistenceService = lectureDaoPersistenceService;
        this.icsCreatorService = icsCreatorService;
        this.icsSaverService = icsSaverService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws IOException {
        List<Lecture> lectures = pdfService.readPdf(null);

        System.out.println("Lecture size: " +  lectures.size());

        List<LectureDao> daos = lectures.stream()
                                        .map(lecture -> dataProcessService.create(lecture))
                                        .collect(Collectors.toList());

        System.out.println("DAO size: " + daos.size());
        lectureDaoPersistenceService.saveAll(daos);
        jsonFileUtil.serializeToFile(daos);

        List<Calendar> calendars = daos.stream()
                                        .map(dao -> icsCreatorService.create(dao))
                                        .flatMap(calenderList -> calenderList.stream())
                                        .peek(calender -> icsSaverService.saveFile(calender, OUTPUT_DIR))
                                        .collect(Collectors.toList());

        System.out.println("Calenders size: " + calendars.size());
    }
}
