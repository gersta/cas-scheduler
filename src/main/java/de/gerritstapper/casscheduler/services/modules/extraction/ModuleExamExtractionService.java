package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.ExamInfo;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.EXAM_HEADLINE;

@Service
@Log4j2
public class ModuleExamExtractionService implements IExtractionHelper {

    public boolean isExam(String line) {
        return matchesHeadlineLowercase(line, EXAM_HEADLINE);
    }

    public ExamInfo extractExam(String examLine) {
        log.debug("extractExam(): {}", examLine);

        String[] examInfo = examLine.split(ExtractionPatterns.WHITESPACE.getPattern());

        // the field exam can be of variable length and complexitiy, but the two remaining fields are consistent
        // take those first
        String examMarking = examInfo[examInfo.length - 1];
        String examDuration = examInfo[examInfo.length - 2];

        StringBuilder exam = new StringBuilder();
        int endExclusive = examInfo.length - 2;
        List<String> examTest = Arrays.stream(examInfo, 0, endExclusive).collect(Collectors.toList());

        for (String part : examTest) {
            exam.append(part);
        }

        return ExamInfo.builder()
                .exam(exam.toString().trim())
                .examDuration(examDuration)
                .examMarking(examMarking)
                .build();
    }

}
