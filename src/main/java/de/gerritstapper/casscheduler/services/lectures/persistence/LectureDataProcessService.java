package de.gerritstapper.casscheduler.services.lectures.persistence;

import de.gerritstapper.casscheduler.daos.lecture.BlockDao;
import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.DatesTuple;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LectureDataProcessService {

    private final DateTimeFormatter DATE_FORMAT;

    public LectureDataProcessService(
            @Value("${cas-scheduler.lectures.pdf.date-pattern}") String datePattern
    ) {
        this.DATE_FORMAT = DateTimeFormatter.ofPattern(datePattern);
    }

    /**
     *
     * @param lecture
     * @return
     */
    public LectureDao create(Lecture lecture) {
        return LectureDao.builder()
                .lectureCode(lecture.getLectureCode())
                .name(lecture.getName())
                .blocks(createBlocks(lecture))
                .build();
    }

    private List<BlockDao> createBlocks(Lecture lecture) {
        List<BlockDao> blocks = new ArrayList<>();

        if ( hasFirstBlock(lecture) ) {
            DatesTuple<LocalDate, LocalDate> firstBlockDates = getDates(lecture.getFirstBlockStart(), lecture.getFirstBlockEnd());

            BlockDao firstBlock = BlockDao.builder()
                    .blockStart(firstBlockDates.getStart())
                    .blockEnd(firstBlockDates.getEnd())
                    .location(lecture.getFirstBlockLocation())
                    .filename(createFirstBlockFileName(lecture, firstBlockDates.getStart()))
                    .singleDayBlock(isSingleDayBlock(lecture.getFirstBlockStart()))
                    .build();

            blocks.add(firstBlock);
        }

        if ( hasSecondBlock(lecture) ) {
            DatesTuple<LocalDate, LocalDate> secondBlockDates = getDates(lecture.getSecondBlockStart(), lecture.getSecondBlockEnd());

            BlockDao secondBlock = BlockDao.builder()
                    .blockStart(secondBlockDates.getStart())
                    .blockEnd(secondBlockDates.getEnd())
                    .location(lecture.getFirstBlockLocation())
                    .filename(createSecondBlockFilename(lecture, secondBlockDates.getStart()))
                    .singleDayBlock(isSingleDayBlock(lecture.getSecondBlockStart()))
                    .build();

            blocks.add(secondBlock);
        }

        return blocks;
    }

    private boolean hasFirstBlock(Lecture lecture) {
        return hasBlock(lecture.getFirstBlockEnd());
    }

    private boolean hasSecondBlock(Lecture lecture) {
        return hasBlock(lecture.getSecondBlockEnd());
    }

    private boolean hasBlock(String end) {
        return !end.isEmpty();
    }


    private String createFirstBlockFileName(Lecture lecture, LocalDate start) {
        return createFileName(lecture, start, "1st-Block");
    }

    private String createSecondBlockFilename(Lecture lecture, LocalDate start) {
        return createFileName(lecture, start, "2nd-Block");
    }

    private String createFileName(Lecture lecture, LocalDate start, String block) {
        return String.format("%s_start_%s_%s.ics", lecture.getName(), start, block);
    }

    /**
     * create proper localdates for the lectures start and end dates
     * @param start: the start date from {@link Lecture}
     * @param end: the end date from {@link Lecture}
     * @return: DatesTuple holding {@link LocalDate} for start and end
     */
    public DatesTuple<LocalDate, LocalDate> getDates(String start, String end) {
        LocalDate endDate = LocalDate.parse(end, DATE_FORMAT);
        int year = endDate.getYear();

        if ( isSingleDayBlock(start) ) {
            return new DatesTuple<>(endDate, endDate);
        }

        start = start + year;
        LocalDate startDate = LocalDate.parse(start, DATE_FORMAT);

        return new DatesTuple<>(startDate, endDate);
    }

    private boolean isSingleDayBlock(String start) {
        return start.isEmpty();
    }
}
