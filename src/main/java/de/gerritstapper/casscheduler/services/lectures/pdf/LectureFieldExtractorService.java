package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.enums.LectureRegexPatterns;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

@Log4j2
public class LectureFieldExtractorService {

    public String getId(String content) {
        return get(content, LectureRegexPatterns.ID);
    }

    public String getName(String content) {
        return content.strip();
    }

    public String getStart(String content) {
        return get(content, LectureRegexPatterns.START);
    }

    public String getEnd(String content) {
        return get(content, LectureRegexPatterns.END);
    }

    public String getLocation(String content) {
        return get(content, LectureRegexPatterns.LOCATION);
    }

    private String get(String content, LectureRegexPatterns pattern) {
        Pattern regex = Pattern.compile(pattern.getPattern());

        var matcher = regex.matcher(content);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }
}
