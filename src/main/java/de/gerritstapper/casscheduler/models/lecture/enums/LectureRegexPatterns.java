package de.gerritstapper.casscheduler.models.lecture.enums;

public enum LectureRegexPatterns {
    // expect ID first + whitepace then
    // any char (a-zA-Z) any digit, parentheses (), whitespaces \s and hyphens -
    // expect whitespace then Start
    NAME("(?<=[A-Z]{1}\\d{1}[A-Z]{1}\\d{5}\\s)(.*?)(?=(\\s\\d\\d\\.))"),
    // xx.xx. followed by an optional whitespace
    START("(?<!Beginn )(\\d\\d)\\.(\\d\\d)\\.(?!\\d\\d\\d\\d)"), // exclude dates after the word "Beginn" as these are just notes in the name of the lecture and should be skipped
    END("(\\d\\d).(\\d\\d).(\\d\\d\\d\\d)"),
    // locations can have between 1 and 3 letters or be a question mark ? in case the location is unknown
    LOCATION("(?<=\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d\\s)([A-Za-z]{1,3}|\\?)"); // TODO: Make this more specific

    private String pattern;

    LectureRegexPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
