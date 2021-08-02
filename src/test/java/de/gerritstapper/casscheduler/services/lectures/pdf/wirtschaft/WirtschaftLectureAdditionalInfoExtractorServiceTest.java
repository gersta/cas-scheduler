package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WirtschaftLectureAdditionalInfoExtractorServiceTest {

    private WirtschaftLectureAdditionalInfoExtractorService additionalInfoExtractorService;

    @BeforeEach
    void beforeEach() {
        additionalInfoExtractorService = new WirtschaftLectureAdditionalInfoExtractorService();
    }

    @Test
    void shouldExtractZertifikationsprogrammFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM I: Anwendungsorientierte Forschung (Zertifikatsprogramm)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("Zertifikatsprogramm", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM I: Anwendungsorientierte Forschung", result.getName())
        );
    }

    @Test
    void shouldExtractSommersemesterFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM I: Anwendungsorientierte Forschung (SoSe)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("SoSe", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM I: Anwendungsorientierte Forschung", result.getName())
        );
    }

    @Test
    void shouldExtractStudyPathFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM I: Anwendungsorientierte Forschung (ACS)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("ACS", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM I: Anwendungsorientierte Forschung", result.getName())
        );
    }

    @Test
    void shouldExtractLearningGroupFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM I: Anwendungsorientierte Forschung (GBM - Gruppe A)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("GBM - Gruppe A", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM I: Anwendungsorientierte Forschung", result.getName())
        );
    }

    @Test
    void shouldExtractLanguageInformationFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM II: Strategisches Management - teilweise in englischer Sprache")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("teilweise in englischer Sprache", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM II: Strategisches Management", result.getName())
        );
    }

    @Test
    void shouldExtractDiscontinuedModuleFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("Wertorientierung und Werteorientierung (letztmalig im Modulangebot)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("letztmalig im Modulangebot", result.getAdditionalInformation().get(0)),
                () -> assertEquals("Wertorientierung und Werteorientierung", result.getName())
        );
    }

    @Test
    void shouldExtractNewModuleInformationFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("Tools für die Strategie-Beratung (neu:MKT)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("neu:MKT", result.getAdditionalInformation().get(0)),
                () -> assertEquals("Tools für die Strategie-Beratung", result.getName())
        );
    }

    @Test
    void shouldReadMultiplePiecesOfInformationSeparatedBySemicolonFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM I: Anwendungsorientierte Forschung (SoSe;Zertifikatsprogramm)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals(2, result.getAdditionalInformation().size()),
                () -> assertEquals("SoSe", result.getAdditionalInformation().get(0)),
                () -> assertEquals("Zertifikatsprogramm", result.getAdditionalInformation().get(1)),
                () -> assertEquals("GM I: Anwendungsorientierte Forschung", result.getName())
        );
    }

    @Test
    void shouldDiscardBlockStartFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("Risikomanagement (Start: 16.10.2020)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertTrue(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("Risikomanagement", result.getName())
        );
    }

    @Test
    void shouldReadLocationAndLanguageInformationFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM II: Strategisches Management (VS) - teilweise in englischer Sprache")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("VS", result.getAdditionalInformation().get(0)),
                () -> assertEquals("teilweise in englischer Sprache", result.getAdditionalInformation().get(1)),
                () -> assertEquals("GM II: Strategisches Management", result.getName())
        );
    }

    @Test
    void shouldExtractLocationWithGermanUmlauteFromLectureName() {
        Lecture lecture = Lecture.builder()
                .name("GM III: Managerial Economics und Recht (LÖ)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("LÖ", result.getAdditionalInformation().get(0)),
                () -> assertEquals("GM III: Managerial Economics und Recht", result.getName())
        );
    }

    @Test
    void shouldSplitInputStringOnSemicolon() {
        Lecture lecture = Lecture.builder()
                .name("GM II: Strategisches Management (Test; MKT)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("Test", result.getAdditionalInformation().get(0)),
                () -> assertEquals("MKT", result.getAdditionalInformation().get(1)),
                () -> assertEquals("GM II: Strategisches Management", result.getName())
        );
    }

    @Test
    void shouldSplitInputStringOnCommaIfNoSemicolonIsPresent() {
        Lecture lecture = Lecture.builder()
                .name("GM II: Strategisches Management (MKT, MDM, PO)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("MKT", result.getAdditionalInformation().get(0)),
                () -> assertEquals("MDM", result.getAdditionalInformation().get(1)),
                () -> assertEquals("PO", result.getAdditionalInformation().get(2)),
                () -> assertEquals("GM II: Strategisches Management", result.getName())
        );
    }

    @Test
    void shouldSplitInputStringOnCommaAndSemicolon() {
        Lecture lecture = Lecture.builder()
                .name("GM II: Strategisches Management (Test; MKT, MDM, PO)")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertFalse(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("Test", result.getAdditionalInformation().get(0)),
                () -> assertEquals("MKT", result.getAdditionalInformation().get(1)),
                () -> assertEquals("MDM", result.getAdditionalInformation().get(2)),
                () -> assertEquals("PO", result.getAdditionalInformation().get(3)),
                () -> assertEquals("GM II: Strategisches Management", result.getName())
        );
    }

    @Test
    void shouldFilterEmptyInputString() {
        Lecture lecture = Lecture.builder()
                .name("")
                .build();

        Lecture result = additionalInfoExtractorService.extractAdditionalInformation(lecture);

        assertAll(
                () -> assertTrue(result.getAdditionalInformation().isEmpty()),
                () -> assertEquals("", result.getName())
        );
    }

}