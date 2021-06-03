package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.enums.WirtschaftLectureRegexPatterns;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

@Log4j2
public class WirtschaftLectureFieldExtractorService {

    public String getId(String content) {
        return get(content, WirtschaftLectureRegexPatterns.ID);
    }

    public String getName(String content) {
        return content.strip();
    }

    public String getStart(String content) {
        return get(content, WirtschaftLectureRegexPatterns.START);
    }

    public String getEnd(String content) {
        return get(content, WirtschaftLectureRegexPatterns.END);
    }

    public String getLocation(String content) {
        return get(content, WirtschaftLectureRegexPatterns.LOCATION);
    }

    private String get(String content, WirtschaftLectureRegexPatterns pattern) {
        Pattern regex = Pattern.compile(pattern.getPattern());

        var matcher = regex.matcher(content);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }
}
