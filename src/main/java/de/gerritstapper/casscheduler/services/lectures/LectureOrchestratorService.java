package de.gerritstapper.casscheduler.services.lectures;

import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.services.lectures.ics.IcsSaverService;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft.WirtschaftLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.persistence.LectureDataProcessService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class LectureOrchestratorService {

    private final String LECTURE_JSON_FILENAME;

    private final TechnikLecturePdfReaderService technikPdfService;
    private final WirtschaftLecturePdfReaderService wirtschaftPdfService;
    private final LectureDataProcessService dataProcessService;
    private final IcsCreatorService icsCreatorService;
    private final IcsSaverService icsSaverService;
    private final JsonFileUtil jsonFileUtil;

    public LectureOrchestratorService(
            @Value("${cas-scheduler.lectures.json.output.filename}") String lecture_json_filename,
            TechnikLecturePdfReaderService technikPdfService,
            WirtschaftLecturePdfReaderService wirtschaftPdfService,
            LectureDataProcessService dataProcessService,
            IcsCreatorService icsCreatorService,
            IcsSaverService icsSaverService,
            JsonFileUtil jsonFileUtil
    ) {
        LECTURE_JSON_FILENAME = lecture_json_filename;
        this.technikPdfService = technikPdfService;
        this.wirtschaftPdfService = wirtschaftPdfService;
        this.dataProcessService = dataProcessService;
        this.icsCreatorService = icsCreatorService;
        this.icsSaverService = icsSaverService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public List<LectureDao> orchestrate() {
        log.info("orchestrate()");

        List<Lecture> lectures = Stream.of(
                technikPdfService,
                wirtschaftPdfService
        )
                .parallel()
                .map(AbstractLecturePdfReaderService::extractLectures)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        log.info("Extracted {} lectures", lectures.size());

        List<LectureDao> daos = lectures.stream()
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
