package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleValidatorServiceTest {

    private ModuleValidatorService validatorService;

    private Module validModule;

    @BeforeEach
    void beforeEach() {
        validatorService = new ModuleValidatorService();

        validModule = Module.builder()
                .lectureCode("T3M30314")
                .lectureName("Automobiltechnik")
                .lectureNameEnglish("Automotive Technolgie")
                .owner("Prof. Dr. Andreas Reichert")
                .duration("1")
                .language("Deutsch/Englisch")
                .lecturingForms("Vorlesung, Uebung")
                .lecturingMethods("Lehrvortrag, Diskussion")
                .exam("Klausur")
                .examDuration("120")
                .examMarking("ja")
                .totalWorkload("150")
                .presentWorkload("50")
                .selfStudyWorkload("100")
                .ectsPoints("5")
                .updatedOn("13.07.2020")
                .build();
    }

    @Test
    void shouldBeValidForValidLectureCode() {
        String validLectureCode = "T3M10507";

        validModule.setLectureCode(validLectureCode);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForInvalidLectureCode() {
        String invalidLectureCode = "ABC";

        validModule.setLectureCode(invalidLectureCode);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLectureCodeNull() {
        String invalidLectureCode = null;

        validModule.setLectureCode(invalidLectureCode);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLectureCodeEmpty() {
        String emptyLectureCode = "";

        validModule.setLectureCode(emptyLectureCode);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForLanguageDeutsch() {
        String language = "Deutsch";

        validModule.setLanguage(language);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForLanguageEnglisch() {
        String language = "Englisch";

        validModule.setLanguage(language);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForLanguageDeutschAndEnglisch() {
        String language = "Deutsch/Englisch";

        validModule.setLanguage(language);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLanguageLatin() {
        String language = "Latin";

        validModule.setLanguage(language);

        assertFalse(validatorService.isValidModule(validModule));
    }
    @Test
    void shouldBeInvalidForLanguageNumeric() {
        String language = "55";

        validModule.setLanguage(language);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLanguageNull() {
        String language = null;

        validModule.setLanguage(language);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLanguageEmpty() {
        String emptyLanguage = "";

        validModule.setLanguage(emptyLanguage);

        assertFalse(validatorService.isValidModule(validModule));
    }


    @Test
    void shouldBeValidForDurationSingleDigit() {
        String duration = "1";

        validModule.setDuration(duration);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForDurationNonSingleDigit() {
        String nonSingleDigitDuration = "10";

        validModule.setDuration(nonSingleDigitDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForAlphaNumericDuration() {
        String duration = "A1";

        validModule.setDuration(duration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLetterDuration() {
        String duration = "Ten";

        validModule.setDuration(duration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForDurationNull() {
        String duration = null;

        validModule.setDuration(duration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForDurationEmpty() {
        String emptyDuration = "";

        validModule.setDuration(emptyDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForValidOwner() {
        String owner = "Prof. Dr.-Ing. Harald Stuhler";

        validModule.setOwner(owner);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForAlphaNumericOwner() {
        String owner = "Prof. Dr.-Ing. 13 Stuhler";

        validModule.setOwner(owner);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForOwnerNull() {
        String owner = null;

        validModule.setOwner(owner);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForOwnerEmpty() {
        String emptyOwner = "";

        validModule.setOwner(emptyOwner);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForLecturingFormAlphabetical() {
        String lecturingForms = "Uebung, Vorlesung";

        validModule.setLecturingForms(lecturingForms);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingFormContainingNumber() {
        String invalidLecturingForms = "2 Vorlesungen";

        validModule.setLecturingForms(invalidLecturingForms);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingFormNull() {
        String invalidLecturingForms = null;

        validModule.setLecturingForms(invalidLecturingForms);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingFormEmpty() {
        String invalidLecturingForms = "";

        validModule.setLecturingForms(invalidLecturingForms);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForLecturingMethodsAlphabetical() {
        String invalidLecturingMethods = "Laborarbeit, Diskussion";

        validModule.setLecturingMethods(invalidLecturingMethods);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingMethodsContainingNumber() {
        String invalidLecturingMethods = "2 Diskussionen";

        validModule.setLecturingMethods(invalidLecturingMethods);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingMethodsNull() {
        String invalidLecturingMethods = null;

        validModule.setLecturingMethods(invalidLecturingMethods);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForLecturingMethodsEmpty() {
        String invalidLecturingMethods = "";

        validModule.setLecturingMethods(invalidLecturingMethods);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForAlphabeticalExam() {
        String validExaml = "Klausur";

        validModule.setExam(validExaml);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamlContainingNumber() {
        String invalidExam = "2 Klausuren";

        validModule.setExam(invalidExam);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamNull() {
        String invalidExam = null;

        validModule.setExam(invalidExam);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamEmpty() {
        String invalidExam = "";

        validModule.setExam(invalidExam);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForExamDurationDigitsOnly() {
        String validExamDuration = "100";

        validModule.setExamDuration(validExamDuration);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamDurationAlphanumeric() {
        String invalidExamDuration = "1h";

        validModule.setExamDuration(invalidExamDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamlDurationAlphabetical() {
        String invalidExamDuration = "Ten";

        validModule.setExamDuration(invalidExamDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamDurationNull() {
        String invalidExamDuration = null;

        validModule.setExamDuration(invalidExamDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamDurationEmpty() {
        String invalidExamDuration = "";

        validModule.setExamDuration(invalidExamDuration);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForExamMarkingUppercaseJa() {
        String validExamMarking = "Ja";

        validModule.setExamMarking(validExamMarking);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForExamMarkingUppercaseNein() {
        String validExamMarking = "Nein";

        validModule.setExamMarking(validExamMarking);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForExamMarkingLowercaseJa() {
        String validExamMarking = "ja";

        validModule.setExamMarking(validExamMarking);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForExamMarkingLowercaseNein() {
        String validExamMarking = "nein";

        validModule.setExamMarking(validExamMarking);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamMarkingAnyTextOtherThanJaOrNein() {
        String inValidExamMarking = "ABC";

        validModule.setExamMarking(inValidExamMarking);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamMarkingNumeric() {
        String inValidExamMarking = "1";

        validModule.setExamMarking(inValidExamMarking);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamMarkingNull() {
        String inValidExamMarking = null;

        validModule.setExamMarking(inValidExamMarking);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForExamMarkingEmpty() {
        String inValidExamMarking = "";

        validModule.setExamMarking(inValidExamMarking);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForTotalWorkloadNumeric() {
        String validTotalWorkload = "120";

        validModule.setTotalWorkload(validTotalWorkload);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForTotalWorkloadAlphanumeric() {
        String inValidTotalWorkload = "12A";

        validModule.setTotalWorkload(inValidTotalWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForTotalWorkloadAlphabetical() {
        String inValidTotalWorkload = "Ten";

        validModule.setTotalWorkload(inValidTotalWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForTotalWorkloadNull() {
        String inValidTotalWorkload = null;

        validModule.setTotalWorkload(inValidTotalWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForTotalWorkloadEmpty() {
        String inValidTotalWorkload = "";

        validModule.setTotalWorkload(inValidTotalWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForPresentWorkloadNumeric() {
        String validPresentWorkload = "120";

        validModule.setPresentWorkload(validPresentWorkload);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForPresentWorkloadAlphanumeric() {
        String inValidPresentWorkload = "12A";

        validModule.setPresentWorkload(inValidPresentWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForPresentWorkloadAlphabetical() {
        String inValidPresentWorkload = "Ten";

        validModule.setPresentWorkload(inValidPresentWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForPresentWorkloadNull() {
        String inValidPresentWorkload = null;

        validModule.setPresentWorkload(inValidPresentWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForPresentWorkloadEmpty() {
        String inValidPresentWorkload = "";

        validModule.setPresentWorkload(inValidPresentWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForSelfStudyWorkloadNumeric() {
        String validSelfStudyWorkload = "120";

        validModule.setSelfStudyWorkload(validSelfStudyWorkload);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForSelfStudyWorkloadAlphanumeric() {
        String inValidSelfStudyWorkload = "12A";

        validModule.setSelfStudyWorkload(inValidSelfStudyWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForSelfStudyWorkloadAlphabetical() {
        String inValidSelfStudyWorkload = "Ten";

        validModule.setSelfStudyWorkload(inValidSelfStudyWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForSelfStudyWorkloadNull() {
        String inValidSelfStudyWorkload = null;

        validModule.setSelfStudyWorkload(inValidSelfStudyWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForSelfStudyWorkloadEmpty() {
        String inValidSelfStudyWorkload = "";

        validModule.setSelfStudyWorkload(inValidSelfStudyWorkload);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForEctsPointsNumeric() {
        String validEctsPoints = "7";

        validModule.setEctsPoints(validEctsPoints);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForEctsPointsAlphanumeric() {
        String inValidEctsPoints = "12A";

        validModule.setEctsPoints(inValidEctsPoints);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForEctsPointsAlphabetical() {
        String inValidEctsPoints = "Ten";

        validModule.setEctsPoints(inValidEctsPoints);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForEctsPointsNull() {
        String inValidEctsPoints = null;

        validModule.setEctsPoints(inValidEctsPoints);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForEctsPointsEmpty() {
        String inValidEctsPoints = "";

        validModule.setEctsPoints(inValidEctsPoints);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeValidForUpdatedOnGermanDateFormat() {
        String validUpdatedOn = "20.04.2017";

        validModule.setUpdatedOn(validUpdatedOn);

        assertTrue(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForUpdatedOnNonGermanDateFormat() {
        String invalidUpdatedOn = "04-20-2017";

        validModule.setUpdatedOn(invalidUpdatedOn);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForUpdatedOnNull() {
        String invalidUpdatedOn = null;

        validModule.setUpdatedOn(invalidUpdatedOn);

        assertFalse(validatorService.isValidModule(validModule));
    }

    @Test
    void shouldBeInvalidForUpdatedOnEmpty() {
        String invalidUpdatedOn = "";

        validModule.setUpdatedOn(invalidUpdatedOn);

        assertFalse(validatorService.isValidModule(validModule));
    }

}