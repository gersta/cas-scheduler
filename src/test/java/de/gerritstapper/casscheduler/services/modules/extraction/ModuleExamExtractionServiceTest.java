package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.ExamInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleExamExtractionServiceTest {

    private ModuleExamExtractionService examExtractionService;

    @BeforeEach
    void beforeEach() {
        examExtractionService = new ModuleExamExtractionService();
    }

    @Test
    void shouldExtractSiehePruefungsordnungAsExamDuration() {
        String content = "Portfolio Siehe Pruefungsordnung ja"; // T3M30224

        ExamInfo examInfo = examExtractionService.extractExam(content);

        assertAll(
                () -> assertNotNull(examInfo),
                () -> assertEquals("Portfolio", examInfo.getExam()),
                () -> assertEquals("Siehe Pruefungsordnung", examInfo.getExamDuration()),
                () -> assertEquals("ja", examInfo.getExamMarking())
        );
    }

    @Test
    void shouldExtractTimeInMinutesForExamDuration() {
        String content = "Portfolio 120 ja"; // T3M30224

        ExamInfo examInfo = examExtractionService.extractExam(content);

        assertAll(
                () -> assertNotNull(examInfo),
                () -> assertEquals("Portfolio", examInfo.getExam()),
                () -> assertEquals("120", examInfo.getExamDuration()),
                () -> assertEquals("ja", examInfo.getExamMarking())
        );
    }

    @Test
    void shouldExtractExamSeminararbeitSlashTransferbericht() {
        String content = "Seminararbeit / Transferbericht Siehe Pruefungsordnung ja"; // T3M10201

        ExamInfo examInfo = examExtractionService.extractExam(content);

        assertAll(
                () -> assertNotNull(examInfo),
                () -> assertEquals("Seminararbeit / Transferbericht", examInfo.getExam()),
                () -> assertEquals("Siehe Pruefungsordnung", examInfo.getExamDuration()),
                () -> assertEquals("ja", examInfo.getExamMarking())
        );
    }

    @Test
    void shouldExtractKombiniertePruefung() {
        String content = "Kombinierte Prüfung - Klausur 75% und Seminararbeit 25 % 90 ja"; // T3M20401

        ExamInfo examInfo = examExtractionService.extractExam(content);

        assertAll(
                () -> assertNotNull(examInfo),
                () -> assertEquals("Kombinierte Prüfung - Klausur 75% und Seminararbeit 25 %", examInfo.getExam()),
                () -> assertEquals("90", examInfo.getExamDuration()),
                () -> assertEquals("ja", examInfo.getExamMarking())
        );
    }

    @Test
    void shouldExtractExamProjektSkizze() {
        String content = "Projekt- bzw. Forschungsskizze Siehe Pruefungsordnung ja"; // T3M10303

        ExamInfo examInfo = examExtractionService.extractExam(content);

        assertAll(
                () -> assertNotNull(examInfo),
                () -> assertEquals("Projekt- bzw. Forschungsskizze", examInfo.getExam()),
                () -> assertEquals("Siehe Pruefungsordnung", examInfo.getExamDuration()),
                () -> assertEquals("ja", examInfo.getExamMarking())
        );
    }

}