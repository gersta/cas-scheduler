package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;

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
                isValidValue(lecture.getName());

    }

    private boolean isValidValue(String value) {
        return !(value.isBlank() || value.isEmpty());
    }
}
