package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;

import java.util.Objects;

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
