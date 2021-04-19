package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.daos.module.ModuleDao;
import de.gerritstapper.casscheduler.models.module.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ModuleDataProcessServiceTest {

    private ModuleDataProcessService dataProcessService;
    private Module module;

    @BeforeEach
    void beforeEach() {
        String dateFormat = "dd.MM.yyyy";
        dataProcessService = new ModuleDataProcessService(dateFormat);

        module = Module.builder()
                .lectureCode("T3M10102")
                .lectureName("Hoehere Festigkeitslehre und Werkstoffmechanik")
                .lectureNameEnglish("Advanced Mechanics")
                .owner("Prof. Dr.-Ing. Petra Bormann")
                .duration("1")
                .language("Duetsch/Englisch")
                .lecturingForms("Vorlesung, Uebung")
                .lecturingMethods("Lehrvortrag, Diskussion, Gruppenarbeit")
                .exam("Klausur")
                .examDuration("120")
                .examMarking("Ja")
                .totalWorkload("150")
                .presentWorkload("50")
                .selfStudyWorkload("100")
                .ectsPoints("5")
                .updatedOn("13.07.2020")
                .build();
    }

    @Test
    void shouldCopyLectureCodeFromModule() {
        module.setLectureCode("T3M10101");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("T3M10101", result.getLectureCode())
        );
    }

    @Test
    void shouldCopyLectureNameFromModule() {
        module.setLectureName("Angewandte Ingenieurmathematik");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Angewandte Ingenieurmathematik", result.getLectureName())
        );
    }

    @Test
    void shouldCopyLectureNameEnglishFromModule() {
        module.setLectureNameEnglish("Applied Engineering Mathematics");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Applied Engineering Mathematics", result.getLectureNameEnglish())
        );
    }

    @Test
    void shouldCopyOwnerFromModule() {
        module.setOwner("Prof. Dr. Volker Schulz");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Prof. Dr. Volker Schulz", result.getOwner())
        );
    }

    @Test
    void shouldConvertStringDurationToInt() {
        module.setDuration("1");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getDuration())
        );
    }

    @Test
    void shouldCopyLanguageFromModule() {
        module.setLanguage("Deutsch/Englisch");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Deutsch/Englisch", result.getLanguage())
        );
    }

    @Test
    void shouldStringConvertLecturingFormsToListOfStrings() {
        module.setLecturingForms("Vorlesung, Uebung");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getLecturingForms().size()),
                () -> assertTrue(result.getLecturingForms().contains("Vorlesung")),
                () -> assertTrue(result.getLecturingForms().contains("Uebung"))
        );
    }

    @Test
    void shouldConvertStringLecturingFormsToListOfStrings() {
        module.setLecturingMethods("Lehrvortrag, Diskussion, Gruppenarbeit");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(3, result.getLecturingMethods().size()),
                () -> assertTrue(result.getLecturingMethods().contains("Lehrvortrag")),
                () -> assertTrue(result.getLecturingMethods().contains("Diskussion")),
                () -> assertTrue(result.getLecturingMethods().contains("Gruppenarbeit"))
        );
    }

    @Test
    void shouldCopyExamFromModule() {
        module.setExam("Klausur");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Klausur", result.getExam())
        );
    }

    @Test
    void shouldCopyExamDurationFromModule() {
        module.setDuration("120");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("120", result.getExamDuration())
        );
    }

    @Test
    void shouldConvertExamMarkingJaUppercaseToBooleanTrue() {
        module.setExamMarking("Ja");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isExamMarking())
        );
    }

    @Test
    void shouldConvertExamMarkingNeinUppercaseToBooleanFalse() {
        module.setExamMarking("Nein");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isExamMarking())
        );
    }

    @Test
    void shouldConvertExamMarkingJaLowercaseToBooleanTrue() {
        module.setExamMarking("ja");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isExamMarking())
        );
    }

    @Test
    void shouldConvertExamMarkingNeinLowercaseToBooleanFalse() {
        module.setExamMarking("nein");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isExamMarking())
        );
    }

    @Test
    void shouldConvertStringTotalWorkloadToInt() {
        module.setTotalWorkload("150");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(150, result.getTotalWorkload())
        );
    }

    @Test
    void shouldConvertStringPresentWorkloadToInt() {
        module.setPresentWorkload("50");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(50, result.getPresentWorkload())
        );
    }

    @Test
    void shouldConvertStringSelfStudyWorkloadToInt() {
        module.setSelfStudyWorkload("100");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(100, result.getSelfStudyWorkload())
        );
    }

    @Test
    void shouldConvertStringEctsPointsToInt() {
        module.setEctsPoints("5");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(5, result.getEctsPoints())
        );
    }

    @Test
    void shouldConvertStringUpdatedOnToLocalDate() {
        module.setUpdatedOn("13.07.2020");

        ModuleDao result = dataProcessService.create(module);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(LocalDate.of(2020, 7, 13), result.getUpdatedOn())
        );
    }

}