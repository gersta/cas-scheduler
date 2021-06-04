package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * this class validates missing information. The pattern of the information (regex) is not checked
 * as the extraction logic is based on exactly these patterns and thus the validation would
 * be redundant
 *
 * Additionally, only ID and name are validated as some lectures only have a single block and thus
 * the start, end and location of the missing block are all empty/blank
 */
@Log4j2
public class WirtschaftLectureValidationService {

    public boolean isValid(Lecture lecture) {
        return isValidValue(lecture.getLectureCode()) &&
                isValidValue(lecture.getName()) &&
                atleastOneValidLocation(lecture) &&
                atleastOneValidEndDate(lecture);
    }

    private boolean isValidValue(String value) {
        return !(value.isBlank() || value.isEmpty());
    }

    private boolean atleastOneValidLocation(Lecture lecture) {
        List<String> locations = List.of(lecture.getLocationOne(), lecture.getLocationTwo());

        return locations.stream().anyMatch(this::isValidValue);
    }

    private boolean atleastOneValidEndDate(Lecture lecture) {
        List<String> endDates = List.of(lecture.getEndOne(), lecture.getEndTwo());

        return endDates.stream().anyMatch(this::isValidValue);
    }
}
