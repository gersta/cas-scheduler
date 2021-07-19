package de.gerritstapper.casscheduler.services.lectures;

import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.DatesTuple;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.persistence.LectureDataProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LectureDataProcessServiceTest {

    private static final String EMPTY = "";

    private LectureDataProcessService dataProcessService;

    @BeforeEach
    void beforeEach() {
        String datePattern = "dd.MM.yyyy";
        dataProcessService = new LectureDataProcessService(datePattern);
    }

    @Test
    void shouldAppendYearForStart() {
        DatesTuple<LocalDate, LocalDate> dates = dataProcessService.getDates("29.08.", "30.08.2022");

        assertEquals(LocalDate.of(2022, 8, 29), dates.getStart());
    }

    @Test
    void shouldCreateLectureDaoFromSingleBlockLecture() {
        var lecture = Lecture.builder()
                .firstBlockStart("20.10.")
                .firstBlockEnd("21.10.2020")
                .secondBlockStart(EMPTY)
                .secondBlockEnd(EMPTY)
                .build();

        LectureDao result = dataProcessService.create(lecture);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getBlocks().size()),
                () -> assertEquals(LocalDate.of(2020, 10, 20), result.getBlocks().get(0).getBlockStart()),
                () -> assertEquals(LocalDate.of(2020, 10, 21), result.getBlocks().get(0).getBlockEnd())
        );
    }

    @Test
    void shouldCreateLectureDaoFromSingleDayBlockLecture() {
        var lecture = Lecture.builder()
                .firstBlockStart(EMPTY)
                .firstBlockEnd("21.10.2020")
                .secondBlockStart(EMPTY)
                .secondBlockEnd(EMPTY)
                .build();

        LectureDao result = dataProcessService.create(lecture);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getBlocks().size()),
                () -> assertTrue(result.getBlocks().get(0).isSingleDayBlock()),
                () -> assertEquals(LocalDate.of(2020, 10, 21), result.getBlocks().get(0).getBlockEnd())
        );
    }

    @Test
    void shouldCreateFilenameContainingLectureNameBlockNumberStartDate() {
        var lecture = Lecture.builder()
                .lectureCode("T3M10001")
                .firstBlockStart("20.10.")
                .firstBlockEnd("21.10.2020")
                .secondBlockStart(EMPTY)
                .secondBlockEnd(EMPTY)
                .build();

        LectureDao result = dataProcessService.create(lecture);

        assertEquals(
                "T3M10001_start_2020-10-20_1st-Block.ics",
                result.getBlocks().get(0).getFilename()
        );
    }

    @Test
    void shouldCopyAdditionalInformationFromLectureToLectureDao() {
        Lecture lecture = Lecture.builder()
                .additionalInformation(Arrays.asList("WiSe", "Zertifikatsprogramm"))
                .firstBlockStart("20.10.")
                .firstBlockEnd("21.10.2020")
                .secondBlockStart(EMPTY)
                .secondBlockEnd(EMPTY)
                .build();

        LectureDao result = dataProcessService.create(lecture);

        assertLinesMatch(Arrays.asList("WiSe", "Zertifikatsprogramm"), result.getAdditionalInformation());
    }
}
