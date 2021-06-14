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
    private final WirtschaftLectureAdditionalInfoExtractorService additionalInfoExtractorService;

    public WirtschaftLecturePdfReaderService(
            CasLecturePdfTextStripper pdfTextStripper,
            WirtschaftLectureValidationService validationService,
            WirtschaftLectureAdditionalInfoExtractorService additionalInfoExtractorService,
            @Value("${cas-scheduler.lectures.pdf.filename.wirtschaft}") String filename
    ) throws IOException {
        super(filename);

        this.pdfTextStripper = pdfTextStripper;
        this.validationService = validationService;
        this.additionalInfoExtractorService = additionalInfoExtractorService;
    }

    @Override
    protected List<Lecture> processPage(PDPage page) {
        log.info("processPage(): {}", page);

        return pdfTextStripper.stripLectures(page, Faculty.WIRTSCHAFT).stream()
                .parallel()
                .filter(validationService::isValid)
                .map(additionalInfoExtractorService::extractAdditionalInformation)
                .collect(Collectors.toList());
    }
}
