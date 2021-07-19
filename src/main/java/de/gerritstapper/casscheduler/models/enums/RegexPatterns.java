package de.gerritstapper.casscheduler.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RegexPatterns {
    LECTURE_CODE("[A-Z]{1}\\d{1}[A-Z]{1}\\d{5}"),
    LINE_BREAK("\r\n|\r|\n");

    @Getter
    private final String pattern;
}
