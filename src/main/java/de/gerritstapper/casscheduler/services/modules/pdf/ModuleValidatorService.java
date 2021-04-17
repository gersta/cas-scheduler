package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
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

        boolean isValid = Objects.nonNull(lectureCode)
                && ( lectureCode.matches(RegexPatterns.LECTURE_CODE.getPattern()) | lectureCode.matches(ModuleRegexPattern.MASTER_THESIS.getPattern()));

        return printIfIsInvalid(isValid, "Lecture Code");
    }

    private boolean isValidLanguage(Module module) {
        String language = module.getLanguage();

        boolean isValid = Objects.nonNull(language)
                && (isDeutsch(language) || isEnglisch(language) || isDeutschAndEnglisch(language));

        return printIfIsInvalid(isValid, "Language");
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

    private boolean isValidDuration(Module module) {
        String duration = module.getDuration();

        boolean isValid = Objects.nonNull(duration) && duration.matches(ModuleRegexPattern.SINGLE_DIGIT.getPattern());

        return printIfIsInvalid(isValid, "Duration");
    }

    private boolean isValidOwner(Module module) {
        String owner = module.getOwner();

        boolean isValid = Objects.nonNull(owner) && owner.matches(ModuleRegexPattern.OWNER.getPattern());

        return printIfIsInvalid(isValid, "Owner");
    }

    private boolean isValidLecturingForms(Module module) {
        String lecturingForms = module.getLecturingForms();

        boolean isValid = Objects.nonNull(lecturingForms)
                && lecturingForms.matches(ModuleRegexPattern.LECTURING_FORMS_METHODS.getPattern());

        return printIfIsInvalid(isValid, "Lecturing Forms");
    }

    private boolean isValidLecturingMethods(Module module) {
        String lecturingMethods = module.getLecturingMethods();

        return Objects.nonNull(lecturingMethods) && lecturingMethods.matches(ModuleRegexPattern.LECTURING_FORMS_METHODS.getPattern());
    }

    private boolean isValidExam(Module module) {
        String exam = module.getExam();

        boolean isValid = Objects.nonNull(exam) && exam.matches(ModuleRegexPattern.LETTERS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Exam");
    }

    private boolean isValidExamDuration(Module module) {
        String examDuration = module.getExamDuration();

        boolean isValid = Objects.nonNull(examDuration) && examDuration.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Exam Duration");
    }

    private boolean isValidExamMarking(Module module) {
        String examMarking = module.getExamMarking();

        boolean isValid = Objects.nonNull(examMarking) && examMarking.matches(ModuleRegexPattern.EXAM_MARKING.getPattern());

        return printIfIsInvalid(isValid, "Exam Marking");
    }

    private boolean isValidTotalWorkload(Module module) {
        String totalWorkload = module.getTotalWorkload();

        boolean isValid = Objects.nonNull(totalWorkload)
                && totalWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Total Workload");
    }

    private boolean isValidPresentWorkload(Module module) {
        String presentWorkload = module.getPresentWorkload();

        boolean isValid = Objects.nonNull(presentWorkload)
                && presentWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Present Workload");
    }

    private boolean isValidSelfStudyWorkload(Module module) {
        String selfStudyWorkload = module.getSelfStudyWorkload();

        boolean isValid = Objects.nonNull(selfStudyWorkload)
                && selfStudyWorkload.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Self Study Workload");
    }

    private boolean isValidEctsPoints(Module module) {
        String ectsPoints = module.getEctsPoints();

        boolean isValid = Objects.nonNull(ectsPoints)
                && ectsPoints.matches(ModuleRegexPattern.DIGITS_ONLY.getPattern());

        return printIfIsInvalid(isValid, "Ects Points");
    }

    private boolean isValidUpdatedOn(Module module) {
        String updatedOn = module.getUpdatedOn();

        boolean isValid = Objects.nonNull(updatedOn) && updatedOn.matches(ModuleRegexPattern.GERMAN_DATE.getPattern());

        return printIfIsInvalid(isValid, "Updated On");
    }

    private boolean printIfIsInvalid(boolean isValid, String fieldName) {
        if ( !isValid ) {
            log.error("{}: {} is invalid", currentLectureCode, fieldName);
        }

        return isValid;
    }
}
