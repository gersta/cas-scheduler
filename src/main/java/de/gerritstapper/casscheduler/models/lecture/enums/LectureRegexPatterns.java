package de.gerritstapper.casscheduler.models.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LectureRegexPatterns {
    ID("(W|T)\\d{1}M\\d{1,5}"),
    START("\\d{2}\\.\\d{2}\\."),
    END("\\d{2}\\.\\d{2}\\.\\d{4}"),
    LOCATION("(\\?|[A-ZÄÜÖ]|[a-zäüö]){1,7}");

    private final String pattern;
}
