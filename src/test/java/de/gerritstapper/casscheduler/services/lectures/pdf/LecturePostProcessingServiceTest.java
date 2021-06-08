package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LecturePostProcessingServiceTest {

    private LecturePostProcessingService postProcessingService;

    @BeforeEach
    void beforeEach() {
        postProcessingService = new LecturePostProcessingService();
    }

    @Test
    void shouldReplaceQuestionmarkLocationWithToBeDefined() {
        var lecture = Lecture.builder()
                                .firstBlockLocation("?")
                                .build();

        Lecture result = postProcessingService.postProcess(lecture);

        assertEquals("TBD", result.getFirstBlockLocation());
    }
}