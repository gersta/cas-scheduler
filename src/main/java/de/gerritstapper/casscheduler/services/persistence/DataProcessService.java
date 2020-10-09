package de.gerritstapper.casscheduler.services.persistence;

import de.gerritstapper.casscheduler.models.DatesTuple;
import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.daos.LectureDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DataProcessService {


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     *
     * @param lecture: the {@link Lecture} extracted from the pdf
     * @return: a list of both the lecture daos creates from the two blocks of the original lecture
     */
    public static List<LectureDao> create(Lecture lecture) {
        return Arrays.asList(createBlock(lecture, true), createBlock(lecture, false));
    }

    /**
     *
     * @param lecture: the {@link Lecture} extracted from the pdf
     * @param isFirstBlock: determines whether the first or the second values (blocks) of a lecture is used
     * @return
     */
    private static LectureDao createBlock(Lecture lecture, boolean isFirstBlock) {
        DatesTuple<LocalDate, LocalDate> dates = isFirstBlock ? getDates(lecture.getStartOne(), lecture.getEndOne()) : getDates(lecture.getStartTwo(), lecture.getEndTwo());

        return LectureDao.builder()
                .id(lecture.getId())
                .name(lecture.getName())
                .startDate(dates.getStart())
                .endDate(dates.getEnd())
                .location(isFirstBlock ? lecture.getLocationOne() : lecture.getLocationTwo())
                .build();
    }

    public static DatesTuple<LocalDate, LocalDate> getDates(String start, String end) {
        LocalDate endDate = LocalDate.parse(end, DATE_FORMAT);
        int year = endDate.getYear();

        start = start + year;
        LocalDate startDate = LocalDate.parse(start, DATE_FORMAT);

        return new DatesTuple<>(startDate, endDate);
    }
}
