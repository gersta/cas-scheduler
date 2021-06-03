package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import de.gerritstapper.casscheduler.models.lecture.enums.LectureRegexPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * convenience service that holds extract methods for each data field of the lecture plan
 * extract the text via regex patterns from given input strings
 */
@Service
@Log4j2
public class TechnikLectureFieldExtractorService {

    /**
     * extract text by given pattern from the content and return
     * @param content: the input string to fetch the data from
     * @param pattern: the pattern of the data field to extract the value for
     * @param position: the occurence of the pattern to look for (e.g second occurence)
     * @return: the value for the pattern at given position in the content string or an empty string in case of no match
     */
    private String get(String content, String pattern, int position) {
        log.trace("get(): {}, {}, {}", content, pattern, position);

        Pattern regex = Pattern.compile(pattern);
        if ( position == 2 ) {
            // replace the first occurrence to only match the second with the pattern
            content = content.replaceFirst(pattern, "");
        }
        Matcher matcher = regex.matcher(content);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }

    public String getId(String content) {
        log.trace("getId(): {}", content);

        return get(content, RegexPatterns.LECTURE_CODE.getPattern(), 1);
    }

    public String getName(String content) {
        log.trace("getName(): {}", content);

        content = content.replace("ö", "oe")
                        .replace("Ö", "Oe")
                        .replace("ü", "ue")
                        .replace("Ü", "Ue")
                        .replace("ä", "ae")
                        .replace("Ä", "Ae");

        return get(content, LectureRegexPatterns.NAME.getPattern(), 1);

    }

    public String getStartOne(String content) {
        log.trace("getStartOne(): {}", content);

        return getStart(content, 1);
    }

    public String getStartTwo(String content) {
        log.trace("getStartTwo(): {}", content);

        return getStart(content, 2);
    }

    private String getStart(String content, int position) {
        log.trace("getStart(): {}, {}", content, position);

        return get(content, LectureRegexPatterns.START.getPattern(), position).replace("-", "").strip();
    }

    public String getEndOne(String content) {
        log.trace("getEndOne(): {}", content);

        return getEnd(content, 1);
    }

    public String getEndTwo(String content) {
        log.trace("getEndTwo(): {}", content);

        return getEnd(content, 2);
    }

    private String getEnd(String content, int position) {
        log.trace("getEnd(): {}, {}", content, position);

        return get(content, LectureRegexPatterns.END.getPattern(), position).replace("-", "").strip();
    }

    public String getLocationOne(String content) {
        log.trace("getLocationOne(): {}", content);

        return getLocation(content, 1);
    }

    public String getLocationTwo(String content) {
        log.trace("getLocationTwo(): {}", content);

        return getLocation(content, 2);
    }

    private String getLocation(String content, int position) {
        log.trace("getLocation(): {}, {}", content, position);

        content = replaceGermanOe(content);
        return get(content, LectureRegexPatterns.LOCATION.getPattern(), position).replace("(", "").replace(")", "");
    }

    private String replaceGermanOe(String input) {
        log.trace("replaceGermanOe(): {}", input);

        return input.replace("Ö", "OE"); // replace the Ö in Lörrach (LÖ)
    }
}
