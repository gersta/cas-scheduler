package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.models.module.enums.ModuleRegexPattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class ModuleValidatorService {

    private static final String ENGLISCH_LANGUAGE = "Englisch";
    private static final String DEUTSCH_LANGUAGE = "Deutsch";
    private static final String DEUTSCH_ENGLISCH_LANGUAGE = "Deutsch/Englisch";

    private String currentLectureCode;

    public boolean isValidModule(Module module) {
        log.trace("isValid(): {}", module);

        if ( Objects.isNull(module) ) {
            return false;
        }

        currentLectureCode = module.getLectureCode();

        return isValidLectureCode(module)
                && isValidName(module)
                && isValidEnglishName(module)
                && isValidLanguage(module)
                && isValidDuration(module)
                && isValidOwner(module)
                && isValidLecturingForms(module)
                && isValidLecturingMethods(module)
                && isValidExam(module)
                && isValidExamDuration(module)
                && isValidExamMarking(module)
                && isValidTotalWorkload(module)
                && isValidPresentWorkload(module)
                && isValidSelfStudyWorkload(module)
                && isValidEctsPoints(module)
                && isValidUpdatedOn(module);
    }

    private boolean isValidLectureCode(Module module) {
        String lectureCode = module.getLectureCode();

        boolean isValid = Objects.nonNull(lectureCode) && !lectureCode.isBlank()
                && (
                        lectureCode.matches(ModuleRegexPattern.LECTURE_CODE.getPattern()) ||
                        lectureCode.matches(ModuleRegexPattern.LECTURE_CODE_MASTER_THESIS.getPattern()) ||
                        lectureCode.matches(ModuleRegexPattern.LECTURE_CODE_MULTIDIS_COMPETENCES.getPattern())
        );

        return printIfIsInvalid(isValid, "Lecture Code", lectureCode);
    }

    private boolean isValidName(Module module) {
        String name = module.getLectureName();
        
        boolean isValid = Objects.nonNull(name) && !name.isEmpty() && !name.isBlank();

        return printIfIsInvalid(isValid, "Lecture Name", name);
    }

    private boolean isValidEnglishName(Module module) {
        String nameEnglish = module.getLectureNameEnglish();

        boolean isValid = Objects.nonNull(nameEnglish) && !nameEnglish.isEmpty() && !nameEnglish.isBlank();

        return printIfIsInvalid(isValid, "Lecture Name English", nameEnglish);
    }

    private boolean isValidLanguage(Module module) {
        String language = module.getLanguage();

        boolean isValid = Objects.nonNull(language) && ( isMasterThesis(module)
                || (
                        isDeutsch(language) ||
                        isEnglisch(language) ||
                        isDeutschAndEnglisch(language) ||
                        language.matches(ModuleRegexPattern.LANGUAGE_NOT_AVAILABLE.getPattern())
        ));

        return printIfIsInvalid(isValid, "Language", language);
    }

    private boolean isDeutsch(String language) {
        return language.equalsIgnoreCase(DEUTSCH_LANGUAGE);
    }

    private boolean isEnglisch(String language) {
        return language.equalsIgnoreCase(ENGLISCH_LANGUAGE);
    }

    private boolean isDeutschAndEnglisch(String language) {
        return language.equalsIgnoreCase(DEUTSCH_ENGLISCH_LANGUAGE);
    }

    private boolean isMasterThesis(Module module) {
        String lectureCode = module.getLectureCode();

        return Objects.nonNull(lectureCode) && lectureCode.matches(ModuleRegexPattern.LECTURE_CODE_MASTER_THESIS.getPattern());
    }

    private boolean isValidDuration(Module module) {
        String duration = module.getDuration();

        boolean isValid = Objects.nonNull(duration) && !duration.isBlank()
                && duration.matches(ModuleRegexPattern.SINGLE_DIGIT.getPattern());

        return printIfIsInvalid(isValid, "Duration", duration);
    }

    private boolean isValidOwner(Module module) {
        String owner = module.getOwner();

        // the owner may be blank
        boolean isValid = Objects.nonNull(owner) && ( owner.isBlank()
                || owner.matches(ModuleRegexPattern.OWNER.getPattern()) );

        return printIfIsInvalid(isValid, "Owner", owner);
    }

    private boolean isValidLecturingForms(Module module) {
        String lecturingForms = module.getLecturingForms();

        boolean isValid = Objects.nonNull(lecturingForms) && !lecturingForms.isBlank()
                && lecturingForms.matches(ModuleRegexPattern.LECTURING_FORMS_METHODS.getPattern());

        return printIfIsInvalid(isValid, "Lecturing Forms", lecturingForms);
    }

    private boolean isValidLecturingMethods(Module module) {
        String lecturingMethods = module.getLecturingMethods();

        boolean isValid = Objects.nonNull(lecturingMethods) && !lecturingMethods.isBlank()
                && lecturingMethods.matches(ModuleRegexPattern.LECTURING_FORMS_METHODS.getPattern());

        return printIfIsInvalid(isValid, "Lecturing Methods", lecturingMethods);
    }

    private boolean isValidExam(Module module) {
        String exam = module.getExam();

        boolean isValid = Objects.nonNull(exam) && !exam.isBlank();

        return printIfIsInvalid(isValid, "Exam", exam);
    }

    private boolean isValidExamDuration(Module module) {
        String examDuration = module.getExamDuration();

        boolean isValid = Objects.nonNull(examDuration) && !examDuration.isBlank()
                && ( examDuration.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern()) | examDuration.matches(ModuleRegexPattern.EXAM_DURATION_PRUEFUNGSORDNUNG.getPattern()));

        return printIfIsInvalid(isValid, "Exam Duration", examDuration);
    }

    private boolean isValidExamMarking(Module module) {
        String examMarking = module.getExamMarking();

        boolean isValid = Objects.nonNull(examMarking) && !examMarking.isBlank()
                && (
                        examMarking.matches(ModuleRegexPattern.EXAM_MARKING.getPattern()) ||
                        examMarking.matches(ModuleRegexPattern.EXAM_MARKING_PASSED_NOT_PASSED.getPattern()) ||
                        examMarking.matches(ModuleRegexPattern.EXAM_MARKING_PARTICIPATED.getPattern())
        );

        return printIfIsInvalid(isValid, "Exam Marking", examMarking);
    }

    private boolean isValidTotalWorkload(Module module) {
        String totalWorkload = module.getTotalWorkload();

        boolean isValid = Objects.nonNull(totalWorkload) && !totalWorkload.isBlank()
                && totalWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Total Workload", totalWorkload);
    }

    private boolean isValidPresentWorkload(Module module) {
        String presentWorkload = module.getPresentWorkload();

        boolean isValid = Objects.nonNull(presentWorkload) && !presentWorkload.isBlank()
                && presentWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Present Workload", presentWorkload);
    }

    private boolean isValidSelfStudyWorkload(Module module) {
        String selfStudyWorkload = module.getSelfStudyWorkload();

        boolean isValid = Objects.nonNull(selfStudyWorkload) && !selfStudyWorkload.isBlank()
                && selfStudyWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Self Study Workload", selfStudyWorkload);
    }

    private boolean isValidEctsPoints(Module module) {
        String ectsPoints = module.getEctsPoints();

        boolean isValid = Objects.nonNull(ectsPoints) && !ectsPoints.isBlank()
                && ectsPoints.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Ects Points", ectsPoints);
    }

    private boolean isValidUpdatedOn(Module module) {
        String updatedOn = module.getUpdatedOn();

        boolean isValid = Objects.nonNull(updatedOn) && !updatedOn.isBlank()
                && updatedOn.matches(ModuleRegexPattern.GERMAN_DATE.getPattern());

        return printIfIsInvalid(isValid, "Updated On", updatedOn);
    }

    private boolean printIfIsInvalid(boolean isValid, String fieldName, String value) {
        if ( !isValid ) {
            log.error("{}: {} is invalid: {}", currentLectureCode, fieldName, value);
        }

        return isValid;
    }
}
