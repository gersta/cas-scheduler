package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.Faculty;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import de.gerritstapper.casscheduler.services.lectures.pdf.LecturePostProcessingService;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * central class to orchestrate the scraping of the content
 */
@Service
@Log4j2
public class TechnikLecturePdfReaderService extends AbstractLecturePdfReaderService {

    // DEPENDENCIES
    private final CasLecturePdfTextStripper pdfTextStripper;
    private final TechnikLectureValidatorService validatorService;
    private final LecturePostProcessingService postProcessingService;
    
    public TechnikLecturePdfReaderService(
            final CasLecturePdfTextStripper pdfTextStripper,
            final TechnikLectureValidatorService validatorService,
            @Value("${cas-scheduler.lectures.pdf.filename.technik}") String filename,
            LecturePostProcessingService postProcessingService) throws IOException {
        super(filename);

        this.pdfTextStripper = pdfTextStripper;
        this.validatorService = validatorService;
        this.postProcessingService = postProcessingService;
    }

    /**
     * walk the given pdf page step-by-step and read the next line
     * removes any invalid rows (lecure instances)
     * @param page: pdf page from the given pdf document
     * @return: list of valid {@link Lecture} instances scraped from the given pdf page
     */
    protected List<Lecture> processPage(PDPage page) {
        log.info("processPage(): {}", page);

        List<Lecture> lectures = pdfTextStripper.stripLectures(page, Faculty.TECHNIK).stream()
                .parallel()
                .filter(validatorService::isValid)
                .map(postProcessingService::postProcess)
                .collect(Collectors.toList());

        log.trace("Read {} valid lectures from page {}", lectures.size(), page);

        return lectures;
    }
}