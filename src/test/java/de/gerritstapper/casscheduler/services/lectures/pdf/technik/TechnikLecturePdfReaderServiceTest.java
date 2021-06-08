package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.technik.TechnikLectureValidatorService;
import de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft.WirtschaftLectureFieldExtractorService;
import de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft.WirtschaftLectureValidationService;
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
        var extractor = new WirtschaftLectureFieldExtractorService();

        service = new TechnikLecturePdfReaderService(
                new CasLecturePdfTextStripper(extractor),
                new TechnikLectureValidatorService(),
                FILENAME
        );
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
        lectures.forEach(System.out::println);
        assertEquals(58, lectures.size());
    }

    @Test
    public void shouldReturn64LectureObjectsForPageFive() throws IOException {
        List<Lecture> lectures = service.extractLectures(4);

        lectures.forEach(System.out::println);

        assertEquals(63, lectures.size());
    }

    @Test
    public void shouldRecognizeBothLectureBlocks() throws IOException {
        Lecture threeDTechnology = service.extractLectures(4).stream()
                                        .filter(lecture -> lecture.getLectureCode().equals("T3M30320"))
                                        .findFirst()
                                        .get();

        System.out.println(threeDTechnology);

        assertAll(
                () -> assertEquals("28.10.", threeDTechnology.getFirstBlockStart()),
                () -> assertEquals("30.10.2021", threeDTechnology.getFirstBlockEnd()),
                () -> assertEquals("29.11.", threeDTechnology.getSecondBlockStart()),
                () -> assertEquals("01.12.2021", threeDTechnology.getSecondBlockEnd())
        );
    }

}
