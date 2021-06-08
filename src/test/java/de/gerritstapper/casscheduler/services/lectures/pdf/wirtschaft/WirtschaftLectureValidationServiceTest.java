package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WirtschaftLectureValidationServiceTest {

    private WirtschaftLectureValidationService validationService;

    private Lecture validLecture;

    @BeforeEach
    void beforeEach() {
        validationService = new WirtschaftLectureValidationService();

        validLecture = Lecture.builder()
                .lectureCode("W3M10001")
                .name("GM I: Anwendungsorientierte Forschung (ACS)")
                .firstBlockStart("08.10.")
                .firstBlockEnd("10.10.2020")
                .firstBlockLocation("HN")
                .secondBlockStart("09.11.")
                .secondBlockEnd("10.11.2020")
                .secondBlockLocation("HN")
                .build();
    }

    @Test
    void shouldDetectValidLectureForInitialSetup() {
        assertTrue(validationService.isValid(validLecture));
    }

    /**
     * ID
     */
    @Test
    void shouldDetectInvalidLectureWithEmptyLectureCode() {
        Lecture invalidLecture = validLecture.toBuilder()
                                    .lectureCode("")
                                    .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectInvalidLectureWithBlankLectureCode() {
        Lecture invalidLecture = validLecture.toBuilder()
                .lectureCode(" ")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    /**
     * Name
     */
    @Test
    void shouldDetectInvalidLectureWithEmptyLectureName() {
        Lecture invalidLecture = validLecture.toBuilder()
                .name("")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectInvalidLectureWithBlankLectureName() {
        Lecture invalidLecture = validLecture.toBuilder()
                .name(" ")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    /**
     * Start & End dates
     */
    @Test
    void shouldDetectInvalidLectureForAllDatesEmpty() {
        Lecture invalidLecture = validLecture.toBuilder()
                .firstBlockStart("")
                .firstBlockEnd("")
                .secondBlockStart("")
                .secondBlockEnd("")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectInvalidLectureForAllDatesBlank() {
        Lecture invalidLecture = validLecture.toBuilder()
                .firstBlockStart(" ")
                .firstBlockEnd(" ")
                .secondBlockStart(" ")
                .secondBlockEnd(" ")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectValidLectureWithAtleastOneEndDatePresent() {
        Lecture alsoValidLecture = validLecture.toBuilder()
                .firstBlockEnd("")
                .secondBlockEnd("10.10.2021")
                .build();

        assertTrue(validationService.isValid(alsoValidLecture));
    }

    /**
     * Location
     */
    @Test
    void shouldDetectInvalidLectureForAllLocationsEmpty() {
        Lecture invalidLecture = validLecture.toBuilder()
                .firstBlockLocation("")
                .secondBlockLocation("")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectInvalidLectureForAllLocationsBlank() {
        Lecture invalidLecture = validLecture.toBuilder()
                .firstBlockLocation(" ")
                .secondBlockLocation(" ")
                .build();

        assertFalse(validationService.isValid(invalidLecture));
    }

    @Test
    void shouldDetectValidLectureWithAtleastOneLocationPresent() {
        Lecture alsoValidLecture = validLecture.toBuilder()
                .firstBlockLocation("HN")
                .secondBlockLocation("")
                .build();

        assertTrue(validationService.isValid(alsoValidLecture));
    }

}