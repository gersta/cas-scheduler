package de.gerritstapper.casscheduler.services.pdf;

import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import org.springframework.stereotype.Service;

/**
 * convenience class to define quality rules for the scraped data
 */
@Service
public class ValidatorService {

    /**
     * checks if a given lecture fulfills the quality standards and is thus valid
     * @param lecture: any lecture scraped from the lecture plan
     * @return: true in case the lecture is valid, false in case it's not
     */
    public boolean isValid(Lecture lecture) {
        return
                isValidId(lecture.getId()) &&
                !lecture.getName().isBlank() &&
                isValidStart(lecture.getStartOne()) &&
                isValidEnd(lecture.getEndOne()) &&
                isValidPlace(lecture.getLocationOne()) &&
                isValidStart(lecture.getStartTwo()) &&
                isValidEnd(lecture.getEndTwo()) &&
                isValidPlace(lecture.getLocationTwo());
    }

    public boolean isValidId(String id) {
        // matches capitalLetter - digit - capitalLetter - 5 digits
        return (!id.isBlank() && id.matches(RegexPatterns.ID.getPattern()));
    }

    public boolean isValidStart(String start) {
        // matches: 08.10. or 01.11. for example
        return (!start.isBlank() && start.matches(RegexPatterns.START.getPattern()));
    }

    public boolean isValidEnd(String end) {
        return (!end.isBlank() && end.matches(RegexPatterns.END.getPattern()));
    }

    public boolean isValidPlace(String place) {
        // matches (RV), (MOS), (S)
        return (!place.isBlank() && place.length() > 0 && place.length() <= 3);
    }
}
