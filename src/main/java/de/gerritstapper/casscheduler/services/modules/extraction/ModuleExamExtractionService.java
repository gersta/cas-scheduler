package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.ExamInfo;
import de.gerritstapper.casscheduler.models.module.enums.ModuleRegexPattern;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.EXAM_HEADLINE;

@Service
@Log4j2
public class ModuleExamExtractionService implements IExtractionHelper {

    public boolean isExam(String line) {
        return matchesHeadlineIgnoreCase(line, EXAM_HEADLINE);
    }

    public ExamInfo extractExam(String examLine) {
        log.debug("extractExam(): {}", examLine);

        if ( examLine.contains(ModuleRegexPattern.EXAM_DURATION_PRUEFUNGSORDNUNG.getPattern() ) ) {
            return extractWithDurationSiehePruefungsordnung(examLine);
        } else {
            return extractWithDurationNumeric(examLine);
        }


    }

    private ExamInfo extractWithDurationSiehePruefungsordnung(String examLine) {
        String[] examInfo = examLine
                            .replace(ModuleRegexPattern.EXAM_DURATION_PRUEFUNGSORDNUNG.getPattern(), "")
                            .split(ExtractionPatterns.WHITESPACE.getPattern());

        String examMarking = examInfo[examInfo.length - 1];

        String exam = concatenateExamArrayInfoParts(examInfo, 0, examInfo.length - 1);

        return ExamInfo.builder()
                .exam(exam)
                .examDuration(ModuleRegexPattern.EXAM_DURATION_PRUEFUNGSORDNUNG.getPattern())
                .examMarking(examMarking)
                .build();
    }

    private ExamInfo extractWithDurationNumeric(String examLine) {
        String[] examInfo = examLine.split(ExtractionPatterns.WHITESPACE.getPattern());

        String examMarking = examInfo[examInfo.length - 1];
        String examDuration = examInfo[examInfo.length - 2];

        String exam = concatenateExamArrayInfoParts(examInfo, 0, examInfo.length - 2);

        return ExamInfo.builder()
                .exam(exam)
                .examDuration(examDuration)
                .examMarking(examMarking)
                .build();
    }

    /**
     * the field exam can be of variable length and complexitiy, but the two remaining fields are consistent take those first
     * @param examInfo
     * @param startIndexInclusive
     * @param endIndexExclusive
     * @return
     */
    private String concatenateExamArrayInfoParts(String[] examInfo, int startIndexInclusive, int endIndexExclusive) {
        String examForm = Arrays.stream(examInfo, startIndexInclusive, endIndexExclusive)
                .collect(Collectors.joining(ExtractionPatterns.WHITESPACE.getPattern()));

        return examForm.trim();
    }

}
