package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.util.DateConverterUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class IcsCreatorService {

    private final DateConverterUtil dateConverterUtil;

    public IcsCreatorService(
            final DateConverterUtil dateConverterUtil
    ) {
        this.dateConverterUtil = dateConverterUtil;
    }

    /**
     * creates a iCalender entry to the lecture
     * @param lecture: {@link LectureDao} object
     * @return: iCalender entry as {@link Calendar}
     */
    public List<Calendar> create(LectureDao lecture) {
        return Arrays.asList(createForBlock(lecture, true), createForBlock(lecture, false));
    }

    private Calendar createForBlock(LectureDao lecture, boolean isFirstBlock) {
        // calender metadata
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//CAS Scheduler//iCal4j 2.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // depending on the block, extract the appropriate data from the data
        LocalDate start = isFirstBlock ? lecture.getFirstBlockStart() : lecture.getSecondBlockStart();
        LocalDate end = isFirstBlock ? lecture.getFirstBlockEnd() : lecture.getSecondBlockEnd();
        String location = isFirstBlock ? lecture.getFirstBlockLocation() : lecture.getSecondBlockLocation();

        // append the information for which block this calender is
        // TODO: make this flexible e.g. in terms of translation?
        String name = String.format("%s - %s", lecture.getName(), isFirstBlock ? "1st Block" : "2nd Block");

        // event details
        VEvent event = new VEvent(
                dateConverterUtil.fromLocalDateToIcalDate(start),
                dateConverterUtil.fromLocalDateToIcalDate(end.plusDays(1)), // the iCal dtend is exclusive (http://microformats.org/wiki/dtend-issue)
                name
        );

        // the id of the crator of the event?
        event.getProperties().add(new Uid("cas-scheduler"));
        event.getProperties().add(new Location(location));

        calendar.getComponents().add(event);

        return calendar;
    }
}
