package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.daos.BlockDao;
import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.util.DateConverterUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class IcsCreatorService {

    private final String ICS_PROD_ID;
    private final String ICS_UID;

    private final DateConverterUtil dateConverterUtil;

    public IcsCreatorService(
            @Value("${cas-scheduler.ics.prod-id}") String icsProdId,
            @Value("${cas-scheduler.ics.uid}") String icsUID,
            final DateConverterUtil dateConverterUtil
    ) {
        this.ICS_PROD_ID = icsProdId;
        this.ICS_UID = icsUID;

        this.dateConverterUtil = dateConverterUtil;
    }

    /**
     * creates a iCalender entry to the lecture
     * @param lecture: {@link LectureDao} object
     * @return: iCalender entry as {@link Calendar}
     */
    public List<IcsCalendarWrapper> create(LectureDao lecture) {
        String lectureName = lecture.getName();

        return IntStream.range(0, lecture.getBlocks().size())
                .mapToObj(i -> createForBlock(lecture.getBlocks().get(i), lectureName, i+1)) // start index at 1
                .collect(Collectors.toList());
    }

    private IcsCalendarWrapper createForBlock(BlockDao block, String lectureName, int index) {
        // calender metadata
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(ICS_PROD_ID));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // event details
        VEvent event = new VEvent(
                dateConverterUtil.fromLocalDateToIcalDate(block.getBlockStart()),
                dateConverterUtil.fromLocalDateToIcalDate(block.getBlockEnd().plusDays(1)), // the iCal dtend is exclusive (http://microformats.org/wiki/dtend-issue)
                String.format("%s_%s-Block", lectureName, index)
        );

        // the id of the creator of the event?
        event.getProperties().add(new Uid(ICS_UID));
        event.getProperties().add(new Location(block.getLocation()));

        calendar.getComponents().add(event);

        return IcsCalendarWrapper.builder()
                .calendar(calendar)
                .filename(block.getFilename())
                .build();
    }
}
