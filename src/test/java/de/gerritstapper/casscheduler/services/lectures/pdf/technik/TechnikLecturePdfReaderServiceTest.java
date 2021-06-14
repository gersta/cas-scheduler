package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import de.gerritstapper.casscheduler.services.lectures.pdf.LectureFieldExtractorService;
import de.gerritstapper.casscheduler.services.lectures.pdf.LecturePostProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TechnikLecturePdfReaderServiceTest {

    private static TechnikLecturePdfReaderService service;
    
    private static final String FILENAME = "M_T_Lecture_All_Pages.pdf";

    @BeforeEach
    void beforeEach() throws IOException {
        var extractor = new LectureFieldExtractorService();

        service = new TechnikLecturePdfReaderService(
                new CasLecturePdfTextStripper(extractor),
                new TechnikLectureValidatorService(),
                FILENAME,
                new LecturePostProcessingService());
    }

    @Test
    public void shouldReturn274LecturesFromAllPages() {
        // create another local copy to not overwrite the global list
        List<Lecture> lectures = service.extractLectures(0);

        assertEquals(274, lectures.size());
    }

}
