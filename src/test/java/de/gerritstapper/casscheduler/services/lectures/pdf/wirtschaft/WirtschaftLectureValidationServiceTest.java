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
                .startOne("08.10.")
                .endOne("10.10.2020")
                .locationOne("HN")
                .startTwo("09.11.")
                .endTwo("10.11.2020")
                .locationTwo("HN")
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

}