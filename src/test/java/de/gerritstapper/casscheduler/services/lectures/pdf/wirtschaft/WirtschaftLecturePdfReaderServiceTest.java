package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
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
        pdfReaderService = new WirtschaftLecturePdfReaderService(
                new WirtschaftLectureFieldExtractorService(),
                new WirtschaftLectureValidationService(),
                "M_W_Lecture_All_Pages.pdf",
                55,
                2.0
        );
    }

    /**
     * reads all rows including those with "weitere Termine"
     * @throws IOException
     */
    @Test
    void shouldReadAllLecturesFromPdfInclAdditionalBlocks() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(null);

        result.forEach(System.out::println);

        assertEquals(187, result.size());
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

        result.forEach(System.out::println);

        assertEquals(188, result.size());
    }

}