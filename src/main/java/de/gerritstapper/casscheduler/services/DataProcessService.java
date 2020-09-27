package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.models.DatesTuple;
import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.daos.LectureDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataProcessService {


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LectureDao create(Lecture lecture) {
        DatesTuple<LocalDate, LocalDate> firstDates = getDates(lecture.getStartOne(), lecture.getEndOne());
        DatesTuple<LocalDate, LocalDate> secondDates = getDates(lecture.getStartTwo(), lecture.getEndTwo());

        return LectureDao.builder()
                    .id(lecture.getId())
                    .name(lecture.getName())
                    .startOne(firstDates.getStart())
                    .endOne(firstDates.getEnd())
                    .placeOne(lecture.getPlaceOne())
                    .startTwo(secondDates.getStart())
                    .endTwo(secondDates.getEnd())
                    .placeTwo(lecture.getPlaceTwo())
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
