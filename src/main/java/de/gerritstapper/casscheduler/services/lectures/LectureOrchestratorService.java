package de.gerritstapper.casscheduler.services.lectures;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsSaverService;
import de.gerritstapper.casscheduler.services.lectures.pdf.LecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.persistence.DataProcessService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LectureOrchestratorService {

    private final LecturePdfReaderService pdfService;
    private final DataProcessService dataProcessService;
    private final IcsCreatorService icsCreatorService;
    private final IcsSaverService icsSaverService;
    private final JsonFileUtil jsonFileUtil;

    public LectureOrchestratorService(LecturePdfReaderService pdfService, DataProcessService dataProcessService, IcsCreatorService icsCreatorService, IcsSaverService icsSaverService, JsonFileUtil jsonFileUtil) {
        this.pdfService = pdfService;
        this.dataProcessService = dataProcessService;
        this.icsCreatorService = icsCreatorService;
        this.icsSaverService = icsSaverService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public void orchestrate() throws IOException {
        log.info("orchestrate()");

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
