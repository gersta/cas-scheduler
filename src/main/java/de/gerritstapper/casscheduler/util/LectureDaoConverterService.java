package de.gerritstapper.casscheduler.util;

import de.gerritstapper.casscheduler.daos.FirestoreLectureDao;
import de.gerritstapper.casscheduler.daos.LectureDao;

import java.time.format.DateTimeFormatter;

public class LectureDaoConverterService {

    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /**
     * @param dao
     * @return
     */
    public static FirestoreLectureDao convert(LectureDao dao) {
        return FirestoreLectureDao.builder()
                .name(dao.getName())
                .startDate(dao.getStartDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .endDate(dao.getEndDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .location(dao.getLocation())
                .build();
    }
}
