package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class LecturePostProcessingService {

    private static final String TO_BE_DEFINED = "TBD";

    public Lecture postProcess(Lecture lecture) {
        if ( isUnknownLocation(lecture.getFirstBlockLocation()) ) {
            lecture.setFirstBlockLocation(TO_BE_DEFINED);
        }

        if ( isUnknownLocation(lecture.getSecondBlockLocation()) ) {
            lecture.setSecondBlockLocation(TO_BE_DEFINED);
        }

        return lecture;
    }

    private boolean isUnknownLocation(String location) {
        return Objects.isNull(location) || location.equalsIgnoreCase("?");
    }
}
