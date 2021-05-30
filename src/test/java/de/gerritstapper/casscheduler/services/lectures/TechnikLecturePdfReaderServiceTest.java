package de.gerritstapper.casscheduler.services.lectures;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikInputDataCleansingService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLectureFieldExtractorService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLectureValidatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TechnikLecturePdfReaderServiceTest {

    private static TechnikLecturePdfReaderService service;
    
    private static final String FILENAME = "M_T_Test_All_Pages.pdf";
    private static final Double LINE_HEIGHT = 2.0;
    private static final Integer MINIMAL_Y_OFFSET = 55;

    @BeforeEach
    void beforeEach() throws IOException {
        service = new TechnikLecturePdfReaderService(
            new TechnikLectureValidatorService(),
            new TechnikLectureFieldExtractorService(),
            new TechnikInputDataCleansingService(),
            FILENAME,
            LINE_HEIGHT,
            MINIMAL_Y_OFFSET
        );
    }

    @AfterEach
    void afterEach() throws IOException {
        service.removeRegions();
        service.closeDocument();
    }

    @Test
    public void shouldReturn57LectureObjectsForPageOne() throws IOException {
        // create another local copy to not overwrite the global list
        List<Lecture> lectures = service.extractLectures(0);
        assertEquals(57, lectures.size());
    }

    @Test
    public void shouldReturn66LectureObjectsForPageTwo() throws IOException {
        List<Lecture> lectures = service.extractLectures(1);
        assertEquals(66, lectures.size());
    }

    @Test
    public void shouldReturn30LectureObjectsForPageThree() throws IOException {
        List<Lecture> lectures = service.extractLectures(2);
        // TODO: decide whether to consider lectures that do not have dates and locations, but rather a note
        assertEquals(30, lectures.size());
    }

    @Test
    public void shouldReturn58LectureObjectsForPageFour() throws IOException {
        List<Lecture> lectures = service.extractLectures(3);
        assertEquals(58, lectures.size());
    }

    @Test
    public void shouldReturn64LectureObjectsForPageFive() throws IOException {
        List<Lecture> lectures = service.extractLectures(4);
        assertEquals(63, lectures.size());
    }

    @Test
    public void shouldReturn274LectureObjectsForAllPages() throws IOException {
        List<Lecture> lectures = service.extractLectures(null);

        assertEquals(274, lectures.size());
    }

    @Test
    public void shouldRecognizeBothLectureBlocks() throws IOException {
        Lecture threeDTechnology = service.extractLectures(4).stream()
                                        .filter(lecture -> lecture.getLectureCode().equals("T3M30320"))
                                        .findFirst()
                                        .get();

        System.out.println(threeDTechnology);

        assertAll(
                () -> assertEquals("28.10.", threeDTechnology.getStartOne()),
                () -> assertEquals("30.10.2021", threeDTechnology.getEndOne()),
                () -> assertEquals("29.11.", threeDTechnology.getStartTwo()),
                () -> assertEquals("01.12.2021", threeDTechnology.getEndTwo())
        );
    }

}
