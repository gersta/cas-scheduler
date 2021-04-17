package de.gerritstapper.casscheduler.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RegexPatterns {
    // T3MX0202, W3MX0202 -> master thesis
    // T3M10202, W3M10202 -> normal lectures
    LECTURE_CODE("[A-Z]{1}\\d{1}[A-Z]{1}([A-Z]{1}\\d{4}|\\d{5})");

    @Getter
    private final String pattern;
}
