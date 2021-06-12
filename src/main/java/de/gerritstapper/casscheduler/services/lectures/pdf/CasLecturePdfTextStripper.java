package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.*;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CasLecturePdfTextStripper {

    private final LectureFieldExtractorService fieldExtractorService;

    public CasLecturePdfTextStripper(
            LectureFieldExtractorService fieldExtractorService
    ) {
        this.fieldExtractorService = fieldExtractorService;
    }

    public List<Lecture> stripLectures(PDPage page, Faculty faculty) {
        log.info("stripLectures(): page {} for faculty {}", page, faculty);

        try {
            List<Lecture> lectures = new CasLecturePdfPageTextStripper(page, fieldExtractorService).stripPage(faculty);

            log.info("stripped {} lectures from page {} for faculty {}", lectures.size(), page, faculty);

            return lectures;
        } catch (IOException exception) {
            log.error("Problem stripping lectures from page {} for faculty {}", page, faculty);

            exception.printStackTrace();

            return Collections.emptyList();
        }

    }


}
