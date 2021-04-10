package de.gerritstapper.casscheduler;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.services.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.services.ics.IcsSaverService;
import de.gerritstapper.casscheduler.services.pdf.PdfReaderService;
import de.gerritstapper.casscheduler.services.persistence.DataProcessService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Log4j2
public class App implements ApplicationRunner {

    private final PdfReaderService pdfService;
    private final DataProcessService dataProcessService;
    private final IcsCreatorService icsCreatorService;
    private final IcsSaverService icsSaverService;
    private final JsonFileUtil jsonFileUtil;

    @Autowired
    public App(
            final PdfReaderService pdfService,
            final DataProcessService dataProcessService,
            final IcsCreatorService icsCreatorService,
            final IcsSaverService icsSaverService,
            final JsonFileUtil jsonFileUtil) {
        this.pdfService = pdfService;
        this.dataProcessService = dataProcessService;
        this.icsCreatorService = icsCreatorService;
        this.icsSaverService = icsSaverService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws IOException {
        log.info("Starting CAS Scheduler");

        log.info("Extracting lectures");

        List<Lecture> lectures = pdfService.readPdf(null);

        log.info("Extracted {} lectures", lectures.size());

        List<LectureDao> daos = lectures.stream()
                .map(dataProcessService::create)
                .collect(Collectors.toList());

        log.info("Created {} daos", daos.size());

        jsonFileUtil.serializeToFile(daos);

        List<IcsCalendarWrapper> calendars = daos.stream()
                .map(icsCreatorService::create)
                .flatMap(Collection::stream)
                .peek(icsSaverService::saveFile)
                .collect(Collectors.toList());

        log.info("Created {} ics files", calendars.size());
    }
}
