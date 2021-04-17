package de.gerritstapper.casscheduler.services.modules.extraction;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ModuleGeneralsExtractionService {

    public String extractLectureName(String line) {
        log.debug("extractLectureName(): {}", line);

        Pattern regex = Pattern.compile(".*(?= \\(\\w*\\))");
        Matcher matcher = regex.matcher(line);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }

    public boolean isLectureName(int lineIndex) {
        return lineIndex == 1;
    }

    public boolean isLectureNameEnglish(int lineIndex) {
        return lineIndex == 2;
    }
}
