package de.gerritstapper.casscheduler.models.module.enums;

import lombok.Getter;

public enum ModuleRegexPattern {
    MASTER_THESIS("[A-Z]{1}\\d{1}[A-Z]{1}[A-Z]{1}\\d{4}"),
    SINGLE_DIGIT("\\d"),
    OWNER("([a-zA-Z]|\\.|\\-|\s){1,}"), // use the {1, } notation to define a min length of 1
    LECTURING_FORMS_METHODS("([a-zA-Z]|,|\\s){1,}"),
    LETTERS_ONLY("(\\w{1,})"),
    DIGITS_ONLY("\\d{1,}"),
    EXAM_DURATION_PRUEFUNGSORDNUNG("Siehe Pruefungsordnung"),
    EXAM_SEMINARBEIT_TRANSFERBERICHT("Seminararbeit\\s{0,1}\\/\\s{0,1}Transferbericht"),
    EXAM_COMBINED("(\\w|\\s|\\-|\\%|\\d)+"),
    EXAM_MARKING("(ja|Ja|Nein|nein)"),
    GERMAN_DATE("\\d{2}\\.\\d{2}\\.\\d{4}");

    @Getter
    private final String pattern;

    ModuleRegexPattern(String pattern) {
        this.pattern = pattern;
    }
}
