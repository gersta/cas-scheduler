package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ModuleGeneralsExtractionService {

    public String extractLectureName(String line) {
        log.debug("extractLectureName(): {}", line);

        return line
                .replaceAll(RegexPatterns.LECTURE_CODE.getPattern(), "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .trim();
    }

    public boolean isLectureName(int lineIndex) {
        return lineIndex == 1;
    }

    public boolean isLectureNameEnglish(int lineIndex, String line) {
        return isLectureNameEnglishSingleLine(lineIndex, line) || isLectureNameEnglishInMultiline(lineIndex);
    }

    /**
     * some modules have longer lecture names and such split the name, lecture code and english name into multiple lines
     * e.g. W3M40007. In that case, the english name is at a later index than usually
     * @return
     */
    private boolean isLectureNameEnglishSingleLine(int index, String line) {
        String lineWithoutParantheses = line
                .replaceAll("\\(", "")
                .replaceAll("\\)", "");

        return index == 2 && !lineWithoutParantheses.matches(RegexPatterns.LECTURE_CODE.getPattern());
    }


    private boolean isLectureNameEnglishInMultiline(int index) {
        return index == 3;
    }
}
