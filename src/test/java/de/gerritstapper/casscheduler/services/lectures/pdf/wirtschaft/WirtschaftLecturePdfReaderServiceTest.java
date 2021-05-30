package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WirtschaftLecturePdfReaderServiceTest {

    private WirtschaftLecturePdfReaderService pdfReaderService;

    @BeforeEach
    void beforeEach() throws IOException {
        pdfReaderService = new WirtschaftLecturePdfReaderService("M_W_Lecture_All_Pages.pdf");
    }

    @Disabled
    void shouldReadAllLecturesFromPdf() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(null);

        assertEquals(188, result.size());
    }

    /**
     * some lectures have an additional line 'weitere Termine' that lists
     * additional blocks. These must be reflected in the final list of lectures
     * as well
     */
    @Disabled
    void shouldReadAllLecturesInclAdditionalDatesFromPdf() throws IOException {
        List<Lecture> result = pdfReaderService.extractLectures(null);

        assertEquals(193, result.size());
    }

}