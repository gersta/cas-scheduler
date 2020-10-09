package de.gerritstapper.casscheduler.services.pdf;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * convenience service that holds extract methods for each data field of the lecture plan
 * extract the text via regex patterns from given input strings
 */
public class FieldExtractorService {

    /**
     * extract text by given pattern from the content and return
     * @param content: the input string to fetch the data from
     * @param pattern: the pattern of the data field to extract the value for
     * @param position: the occurence of the pattern to look for (e.g second occurence)
     * @return: the value for the pattern at given position in the content string or an empty string in case of no match
     */
    private static String get(String content, String pattern, int position) {
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

    public static String getId(String content) {
        return get(content, RegexPatterns.ID.getPattern(), 1);
    }

    public static String getName(String content) {
        content = content.replace("ö", "oe")
                        .replace("Ö", "Oe")
                        .replace("ü", "ue")
                        .replace("Ü", "Ue")
                        .replace("ä", "ae")
                        .replace("Ä", "Ae");

        return get(content, RegexPatterns.NAME.getPattern(), 1);

    }

    public static String getStartOne(String content) {
        return getStart(content, 1);
    }

    public static String getStartTwo(String content) {
        return getStart(content, 2);
    }

    private static String getStart(String content, int position) {
        return get(content, RegexPatterns.START.getPattern(), position).replace("-", "").strip();
    }

    public static String getEndOne(String content) {
        return getEnd(content, 1);
    }

    public static String getEndTwo(String content) {
        return getEnd(content, 2);
    }

    private static String getEnd(String content, int position) {
        return get(content, RegexPatterns.END.getPattern(), position).replace("-", "").strip();
    }

    public static String getPlaceOne(String content) {
        return getPlace(content, 1);
    }

    public static String getPlaceTwo(String content) {
        return getPlace(content, 2);
    }

    private static String getPlace(String content, int position) {
        content = content.replace("Ö", "OE"); // replace the Ö in Lörrach (LÖ)
        return get(content, RegexPatterns.PLACE.getPattern(), position).replace("(", "").replace(")", "");
    }
}
