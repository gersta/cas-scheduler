package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.models.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ModulesPdfReaderServiceTest {

    private ModulesPdfReaderService pdfReaderService;

    @BeforeEach
    void beforeEach() throws IOException {
        String filename = "M_T_Modulhandbuch_T3M10101.pdf";
        pdfReaderService = new ModulesPdfReaderService(filename);
    }

    @Test
    void shouldExtractLectureCode() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("T3M10101", module.getLectureCode())
        );
    }

    @Test
    void shouldExtractLectureName() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Angewandte Ingenieurmathematik", module.getLectureName())
        );
    }

    @Disabled
    void shouldNotFailOnNullLectureName() {
        fail();
    }

    @Test
    void shouldExtractLectureNameEnglish() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Applied Engineering Mathematics", module.getLectureNameEnglish())
        );
    }

    @Test
    void shouldExtractLecturer() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Prof. Dr. Volker Schulz", module.getLecturer())
        );
    }

    @Test
    void shouldExtractDuration() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("1", module.getDuration())
        );
    }

    @Test
    void shouldExtractExam() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Klausur", module.getExam())
        );
    }

    @Test
    void shouldExtractExamDuration() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("120", module.getExamDuration())
        );
    }

    @Test
    void shouldExtractExamMarking() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("ja", module.getExamMarking())
        );
    }

    @Test
    void shouldExtractTotalWorkload() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("150", module.getTotalWorkload())
        );
    }

    @Test
    void shouldExtractPresentWorkload() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("50", module.getPresentWorkload())
        );
    }

    @Test
    void shouldExtractSelfstudyWorkload() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("100", module.getSelfStudyWorkload())
        );
    }

    @Test
    void shouldExtractEctsPoints() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("5", module.getEctsPoints())
        );
    }

    @Test
    void shouldExtractUpdatedOn() {
        Module module = pdfReaderService.readPdf().get(0);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("13.07.2020", module.getUpdatedOn())
        );
    }
}