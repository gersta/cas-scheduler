package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.LectureModuleDao;
import de.gerritstapper.casscheduler.daos.lecture.BlockDao;
import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.daos.module.ModuleDao;
import de.gerritstapper.casscheduler.services.merging.LectureModuleMergerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectureModuleMergerServiceTest {

    private LectureModuleMergerService lectureModuleMergerService;

    @BeforeEach
    void beforeEach() {
        lectureModuleMergerService = new LectureModuleMergerService();
    }

    @Test
    void shouldReturnMergedDaoWithLectureAndModuleHavingSameLectureCode() {
        String lectureCode = "T3M10101";

        LectureDao lecture = LectureDao.builder()
                .lectureCode(lectureCode)
                .build();

        ModuleDao module = ModuleDao.builder()
                .lectureCode(lectureCode)
                .build();

        List<LectureModuleDao> result = lectureModuleMergerService.mergeLecturesWithModules(
                Collections.singletonList(lecture),
                Collections.singletonList(module)
        );

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("T3M10101", result.get(0).getLectureCode())
        );
    }

    @Test
    void shouldReturnMergedDaoWithModuleInformation() {
        String lectureCode = "T3M10101";
        String lectureName = "Angewandte Ingenieurmathematik";

        List<BlockDao> lectureBlocks = Arrays.asList(
                BlockDao.builder()
                        .blockStart(LocalDate.of(2021, 5, 2))
                        .blockEnd(LocalDate.of(2021, 5, 5))
                        .location("HN")
                        .filename("Angewandte Ingenieurmathematik_first.ics")
                        .build(),
                BlockDao.builder()
                        .blockStart(LocalDate.of(2021, 6, 2))
                        .blockEnd(LocalDate.of(2021, 6, 5))
                        .location("MA")
                        .filename("Angewandte Ingenieurmathematik_second.ics")
                        .build()
        );

        LectureDao lecture = LectureDao.builder()
                .lectureCode(lectureCode)
                .name(lectureName)
                .blocks(lectureBlocks)
                .build();

        ModuleDao module = ModuleDao.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureNameEnglish("Applied Engineering Mathematics")
                .duration(1)
                .owner("Prof. Dr. Volker Schulz")
                .language("Deutsch/Englisch")
                .lecturingForms(Arrays.asList("Vorlesung", "Uebung"))
                .lecturingMethods(Arrays.asList("Lehrvortrag", "Diskussion", "Gruppenarbeit"))
                .exam("Klausur")
                .examDuration("120")
                .examMarking(true)
                .totalWorkload(150)
                .presentWorkload(50)
                .selfStudyWorkload(100)
                .ectsPoints(5)
                .updatedOn(LocalDate.of(2020, 7, 13))
                .build();

        LectureModuleDao lectureModule = lectureModuleMergerService.mergeLecturesWithModules(
                Collections.singletonList(lecture),
                Collections.singletonList(module)
        ).get(0);

        assertAll(
                () -> assertEquals("T3M10101", lectureModule.getLectureCode()),
                () -> assertEquals("Angewandte Ingenieurmathematik", lectureModule.getLectureName()),
                () -> assertEquals(lectureBlocks, lectureModule.getBlocks()),
                () -> assertTrue(lectureModule.isModuleAvailable()),
                () -> assertEquals("Applied Engineering Mathematics", lectureModule.getLectureNameEnglish()),
                () -> assertEquals(1, lectureModule.getDuration()),
                () -> assertEquals("Prof. Dr. Volker Schulz", lectureModule.getOwner()),
                () -> assertEquals("Deutsch/Englisch", lectureModule.getLanguage()),
                () -> assertLinesMatch(Arrays.asList("Vorlesung", "Uebung"), lectureModule.getLecturingForms()),
                () -> assertLinesMatch(Arrays.asList("Lehrvortrag", "Diskussion", "Gruppenarbeit"), lectureModule.getLecturingMethods()),
                () -> assertEquals("Klausur", lectureModule.getExam()),
                () -> assertEquals("120", lectureModule.getExamDuration()),
                () -> assertTrue(lectureModule.isExamMarking()),
                () -> assertEquals(150, lectureModule.getTotalWorkload()),
                () -> assertEquals(50, lectureModule.getPresentWorkload()),
                () -> assertEquals(100, lectureModule.getSelfStudyWorkload()),
                () -> assertEquals(5, lectureModule.getEctsPoints()),
                () -> assertEquals(LocalDate.of(2020, 7, 13), lectureModule.getUpdatedOn())
        );
    }

    @Test
    void shouldReturnLecuteModuleFlaggedForMissingModule() {
        LectureDao lecture = LectureDao.builder()
                .lectureCode("ABC")
                .build();

        LectureModuleDao resultWithoutModule = lectureModuleMergerService.mergeLecturesWithModules(
                Collections.singletonList(lecture),
                Collections.emptyList()
        ).get(0);

        assertAll(
                () -> assertNotNull(resultWithoutModule),
                () -> assertEquals("ABC", resultWithoutModule.getLectureCode()),
                () -> assertFalse(resultWithoutModule.isModuleAvailable())
        );
    }

}