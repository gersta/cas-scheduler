package de.gerritstapper.casscheduler.services.lectures.persistence;

import de.gerritstapper.casscheduler.daos.lecture.BlockDao;
import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.models.lecture.DatesTuple;
import de.gerritstapper.casscheduler.models.lecture.Lecture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        DatesTuple<LocalDate, LocalDate> firstBlockDates = getDates(lecture.getFirstBlockStart(), lecture.getFirstBlockEnd());
        DatesTuple<LocalDate, LocalDate> secondBlockDates = getDates(lecture.getSecondBlockStart(), lecture.getSecondBlockEnd());

        // TODO: refactor this to be versatile on the number of blocks
        BlockDao firstBlock = BlockDao.builder()
                                .blockStart(firstBlockDates.getStart())
                                .blockEnd(firstBlockDates.getEnd())
                                .location(lecture.getFirstBlockLocation())
                                .filename(createFileName(lecture, firstBlockDates.getStart(), true))
                                .build();

        BlockDao secondBlock = BlockDao.builder()
                                .blockStart(secondBlockDates.getStart())
                                .blockEnd(secondBlockDates.getEnd())
                                .location(lecture.getFirstBlockLocation())
                                .filename(createFileName(lecture, secondBlockDates.getStart(), false))
                                .build();

        return Arrays.asList(firstBlock, secondBlock);
    }

    /**
     * creathe a ICS filename for the Block for later downloading
     * @param lecture: the {@link Lecture} the block belongs to
     * @param start: start date of the block
     * @param isFirstBlock: whether its the first or second block of the lecture
     * @return: filename specific to this block
     */
    private String createFileName(Lecture lecture, LocalDate start, boolean isFirstBlock) {
        return String.format("%s_%s_start_%s.ics", lecture.getName(), isFirstBlock ? "1st Block" : "2nd Block", start);
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

        start = start + year;
        LocalDate startDate = LocalDate.parse(start, DATE_FORMAT);

        return new DatesTuple<>(startDate, endDate);
    }
}
