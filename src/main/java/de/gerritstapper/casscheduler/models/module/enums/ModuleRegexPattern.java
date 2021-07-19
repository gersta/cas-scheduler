package de.gerritstapper.casscheduler.models.module.enums;

import lombok.Getter;

public enum ModuleRegexPattern {
    LECTURE_CODE("[A-Z]{1}\\d{1}[A-Z]{1}\\d{5}"),
    LECTURE_CODE_MASTER_THESIS("[A-Z]{1}\\d{1}[A-Z]{1}[A-Z]{1}\\d{4}"),
    LECTURE_CODE_MULTIDIS_COMPETENCES("XMX0101"),
    SINGLE_DIGIT("\\d"),
    OWNER("(\\p{L}|\\s|\\.|-)+"), // use the {1, } notation to define a min length of 1
    LECTURING_FORMS_METHODS("([a-zA-Z]|,|\\s){1,}"),
    LETTERS_ONLY("(\\w{1,})"),
    DIGITS_ONLY("\\d{1,}"),
    EXAM_DURATION_PRUEFUNGSORDNUNG("Siehe Pruefungsordnung"),
    EXAM_MARKING("(ja|Ja|Nein|nein)"),
    EXAM_MARKING_PASSED_NOT_PASSED("Bestanden/ Nicht-Bestanden"),
    EXAM_MARKING_PARTICIPATED("Teilgenommen"),
    GERMAN_DATE("\\d{2}\\.\\d{2}\\.\\d{4}");

    @Getter
    private final String pattern;

    ModuleRegexPattern(String pattern) {
        this.pattern = pattern;
    }
}
