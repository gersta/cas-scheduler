package de.gerritstapper.casscheduler.services.lectures;

import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsSaverService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.persistence.LectureDataProcessService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LectureOrchestratorService {

    private final String LECTURE_JSON_FILENAME;

    private final TechnikLecturePdfReaderService technikPdfService;
    private final LectureDataProcessService dataProcessService;
    private final IcsCreatorService icsCreatorService;
    private final IcsSaverService icsSaverService;
    private final JsonFileUtil jsonFileUtil;

    public LectureOrchestratorService(
            @Value("${cas-scheduler.lectures.json.output.filename}") String lecture_json_filename,
            TechnikLecturePdfReaderService technikPdfService,
            LectureDataProcessService dataProcessService,
            IcsCreatorService icsCreatorService,
            IcsSaverService icsSaverService,
            JsonFileUtil jsonFileUtil
    ) {
        LECTURE_JSON_FILENAME = lecture_json_filename;
        this.technikPdfService = technikPdfService;
        this.dataProcessService = dataProcessService;
        this.icsCreatorService = icsCreatorService;
        this.icsSaverService = icsSaverService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public List<LectureDao> orchestrate() throws IOException {
        log.info("orchestrate()");

        List<Lecture> technikLectures = technikPdfService.extractLectures(null);

        log.info("Extracted {} lectures", technikLectures.size());

        List<LectureDao> daos = technikLectures.stream()
                .map(dataProcessService::create)
                .collect(Collectors.toList());

        log.info("Created {} DAOs", daos.size());

        jsonFileUtil.serializeToFile(daos, LECTURE_JSON_FILENAME);

        log.info("Written {} DAOs to file: {}", daos.size(), LECTURE_JSON_FILENAME);

        List<IcsCalendarWrapper> calendars = daos.stream()
                .map(icsCreatorService::create)
                .flatMap(Collection::stream)
                .peek(icsSaverService::saveFile)
                .collect(Collectors.toList());

        log.info("Created {} ics files", calendars.size());

        return daos;
    }
}
