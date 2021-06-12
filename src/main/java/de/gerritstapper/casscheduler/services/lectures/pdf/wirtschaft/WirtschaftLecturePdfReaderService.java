package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.Faculty;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class WirtschaftLecturePdfReaderService extends AbstractLecturePdfReaderService {

    private final CasLecturePdfTextStripper pdfTextStripper;
    private final WirtschaftLectureValidationService validationService;

    public WirtschaftLecturePdfReaderService(
            CasLecturePdfTextStripper pdfTextStripper,
            WirtschaftLectureValidationService validationService,
            @Value("${cas-scheduler.lectures.pdf.filename.wirtschaft}") String filename
    ) throws IOException {
        super(filename);

        this.pdfTextStripper = pdfTextStripper;
        this.validationService = validationService;
    }

    @Override
    protected List<Lecture> processPage(PDPage page) {
        log.info("processPage(): {}", page);

        return pdfTextStripper.stripLectures(page, Faculty.WIRTSCHAFT).stream()
                .parallel()
                .filter(validationService::isValid)
                .collect(Collectors.toList());
    }
}
