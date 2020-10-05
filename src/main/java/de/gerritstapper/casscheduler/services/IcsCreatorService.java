package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.util.DateConverterUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

public class IcsCreatorService {

    /**
     * creates a iCalender entry to the lecture
     * @param lecture: {@link LectureDao} object
     * @return: iCalender entry as {@link Calendar}
     */
    public static Calendar create(LectureDao lecture) {
        // calender metadata
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//CAS Scheduler//iCal4j 2.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // event details
        VEvent event = new VEvent(
                DateConverterUtil.fromLocalDateToIcalDate(lecture.getStartDate()),
                DateConverterUtil.fromLocalDateToIcalDate(lecture.getEndDate().plusDays(1)), // the iCal dtend is exclusive (http://microformats.org/wiki/dtend-issue)
                lecture.getName());

        // the id of the crator of the event?
        event.getProperties().add(new Uid("cas-scheduler"));
        event.getProperties().add(new Location(lecture.getLocation()));

        calendar.getComponents().add(event);

        return calendar;
    }
}
