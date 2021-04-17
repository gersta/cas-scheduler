package de.gerritstapper.casscheduler.services.modules.extraction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExtractionPatterns {
    LINEBREAK("\\r?\\n"),
    WHITESPACE(" ");

    @Getter
    private final String pattern;
}
