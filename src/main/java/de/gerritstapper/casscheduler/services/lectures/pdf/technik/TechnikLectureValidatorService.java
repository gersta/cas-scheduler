package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.LectureRegexPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * convenience class to define quality rules for the scraped data
 */
@Service
@Log4j2
public class TechnikLectureValidatorService {

    /**
     * checks if a given lecture fulfills the quality standards and is thus valid
     * @param lecture: any lecture scraped from the lecture plan
     * @return: true in case the lecture is valid, false in case it's not
     */
    public boolean isValid(Lecture lecture) {
        log.trace("isValid(): {}", lecture);

        return
                isValidId(lecture.getLectureCode()) &&
                !lecture.getName().isBlank() &&
                isValidStart(lecture.getFirstBlockStart()) &&
                isValidEnd(lecture.getFirstBlockEnd()) &&
                isValidPlace(lecture.getFirstBlockLocation()) &&
                isValidStart(lecture.getSecondBlockStart()) &&
                isValidEnd(lecture.getSecondBlockEnd()) &&
                isValidPlace(lecture.getSecondBlockLocation());
    }

    public boolean isValidId(String id) {
        log.trace("isValidId(): {}", id);

        // matches capitalLetter - digit - capitalLetter - 5 digits
        return (!id.isBlank() && id.matches(LectureRegexPatterns.ID.getPattern()));
    }

    public boolean isValidStart(String start) {
        log.trace("isValidStart(): {}", start);

        // matches: 08.10. or 01.11. for example
        return (!start.isBlank() && start.matches(LectureRegexPatterns.START.getPattern()));
    }

    public boolean isValidEnd(String end) {
        log.trace("isValidEnd(): {}", end);

        return (!end.isBlank() && end.matches(LectureRegexPatterns.END.getPattern()));
    }

    public boolean isValidPlace(String place) {
        log.trace("isValidPlace(): {}", place);

        return (!place.isBlank() && place.matches(LectureRegexPatterns.LOCATION.getPattern()));
    }
}
