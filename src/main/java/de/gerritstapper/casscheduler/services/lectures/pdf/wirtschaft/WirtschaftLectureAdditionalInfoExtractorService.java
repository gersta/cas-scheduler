package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Service
public class WirtschaftLectureAdditionalInfoExtractorService {

    private static final String ADDITIONAL_INFO_PATTERN = "(?<=\\().*(?=\\))";
    private static final String START_DATE_PATTERN = "Start: \\d{2}\\.\\d{2}\\.\\d{4}";
    private static final String LANGUAGE_INFORMATION = "- teilweise in englischer Sprache";

    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";
    private static final String EMPTY_FALLBACK = "";

    public Lecture extractAdditionalInformation(Lecture lecture) {
        List<String> additionalInfo = extractAddInformationFromLectureName(lecture.getName());

        if ( containsLanguageInformation(lecture.getName()) ) {
            additionalInfo.add(
                    LANGUAGE_INFORMATION
                    .replace("-", "")
                    .trim()
            );
        }

        return lecture.toBuilder()
                .name(removeAdditionalInformationFromLectureName(lecture.getName()))
                .additionalInformation(additionalInfo)
                .build();
    }

    private List<String> extractAddInformationFromLectureName(String name) {
        String additionalInformation = matchInformationInParantheses(name);

        if ( isStartDateInformation(additionalInformation) ) {
            return new ArrayList<>();
        }


        return Arrays.stream(additionalInformation.split(SEMICOLON))
                .flatMap(value -> Arrays.stream(value.split(COMMA)))
                .filter(value -> !value.matches(EMPTY_FALLBACK))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private boolean isStartDateInformation(String additionalInformation) {
        return additionalInformation.matches(START_DATE_PATTERN);
    }

    private String matchInformationInParantheses(String name) {
        Pattern regex = Pattern.compile(ADDITIONAL_INFO_PATTERN);

        Matcher matcher = regex.matcher(name);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return EMPTY_FALLBACK;
    }

    private boolean containsLanguageInformation(String name) {
        return name.contains(LANGUAGE_INFORMATION);
    }

    private String removeAdditionalInformationFromLectureName(String name) {
        return name
                .replaceAll(ADDITIONAL_INFO_PATTERN, "")
                .replaceAll(LANGUAGE_INFORMATION, "")
                .replace("(", "")
                .replace(")", "")
                .trim();
    }
}
