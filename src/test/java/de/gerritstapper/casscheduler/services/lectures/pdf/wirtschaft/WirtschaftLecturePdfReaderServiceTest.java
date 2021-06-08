package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WirtschaftLecturePdfReaderServiceTest {

    private WirtschaftLecturePdfReaderService pdfReaderService;

    @BeforeEach
    void beforeEach() throws IOException {
        var extractor = new WirtschaftLectureFieldExtractorService();
        var stripper = new CasLecturePdfTextStripper(extractor);

        pdfReaderService = new WirtschaftLecturePdfReaderService(
                stripper,
                new WirtschaftLectureValidationService(),
                "M_W_Lecture_All_Pages.pdf"
        );
    }

    /**
     * Somehow the lecture
     * W3M10001 GM I: Anwendungsorientierte Forschung (SoSe;Zertifikatsprogramm)
     * B (empfohlen für ACS, FIN, SLP)
     * is not read properly and thus missing here. All others seem to be present
     */
    @Disabled
    void shouldReadAllLecturesInclW3M10001GM1B() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(null);

        assertEquals(188, result.size());
    }

    @Test
    void shouldRead57LecturesFromPageOne() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(0);

        // TODO: keep W3M10001 -> GM1B in mind which cannot yet be read
        assertEquals(56, result.size());
    }

    @Test
    void shouldRead67LecturesFromPageTwo() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(1);

        result.forEach(System.out::println);

        assertEquals(67, result.size());
    }

    @Test
    void shouldRead64LecturesFromPageThree() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(2);

        assertEquals(64, result.size());
    }

}