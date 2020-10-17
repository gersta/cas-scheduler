package de.gerritstapper.casscheduler.services.persistence;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.DatesTuple;
import de.gerritstapper.casscheduler.models.Lecture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DataProcessService {

    private final DateTimeFormatter DATE_FORMAT;

    public DataProcessService(
            @Value("${cas-scheduler.pdf.date-pattern}") String datePattern
    ) {
        this.DATE_FORMAT = DateTimeFormatter.ofPattern(datePattern);
    }

    /**
     *
     * @param lecture: the {@link Lecture} extracted from the pdf
     * @return: a {@link LectureDao} with the data of the provided lecture
     */
    public LectureDao create(Lecture lecture) {
        DatesTuple<LocalDate, LocalDate> firstBlockDates = getDates(lecture.getStartOne(), lecture.getEndOne());
        DatesTuple<LocalDate, LocalDate> secondBlockDates = getDates(lecture.getStartTwo(), lecture.getEndTwo());

        return LectureDao.builder()
                .lectureCode(lecture.getLectureCode())
                .name(lecture.getName())
                .firstBlockStart(firstBlockDates.getStart())
                .firstBlockEnd(firstBlockDates.getEnd())
                .firstBlockLocation(lecture.getLocationOne())
                .secondBlockStart(secondBlockDates.getStart())
                .secondBlockEnd(secondBlockDates.getEnd())
                .secondBlockLocation(lecture.getLocationTwo())
                .build();
    }

    public DatesTuple<LocalDate, LocalDate> getDates(String start, String end) {
        LocalDate endDate = LocalDate.parse(end, DATE_FORMAT);
        int year = endDate.getYear();

        start = start + year;
        LocalDate startDate = LocalDate.parse(start, DATE_FORMAT);

        return new DatesTuple<>(startDate, endDate);
    }
}
