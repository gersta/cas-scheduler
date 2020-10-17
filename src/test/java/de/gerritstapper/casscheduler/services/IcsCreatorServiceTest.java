package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.BlockDao;
import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.models.IcsCalendarWrapper;
import de.gerritstapper.casscheduler.services.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.util.DateConverterUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IcsCreatorServiceTest {

    // unit under test
    private IcsCreatorService icsCreatorService;

    private LectureDao dao;

    private List<Calendar> calendars;
    private Calendar calendar;
    private CalendarComponent event;

    private final LocalDate START = LocalDate.of(2020, 9, 27);
    private final LocalDate END = LocalDate.of(2020, 9, 28);


    @BeforeEach
    void beforeEach() {
        icsCreatorService = new IcsCreatorService("-//CAS Scheduler//iCal4j 2.0//EN", "cas-scheduler", new DateConverterUtil());

        dao = LectureDao.builder()
                            .lectureCode("T3M10010")
                            .name("Angewandte Ingenieurmathematik")
                            .blocks(Arrays.asList(
                                    BlockDao.builder()
                                            .blockStart(START)
                                            .blockEnd(END)
                                            .location("S")
                                            .build(),
                                    BlockDao.builder()
                                            .blockStart(START.plusDays(1))
                                            .blockEnd(END.plusDays(1))
                                            .location("S")
                                            .build()
                            ))
                            .build();

        List<IcsCalendarWrapper> wrappers = icsCreatorService.create(dao);
        calendars = wrappers.stream().map(wrapper -> wrapper.getCalendar()).collect(Collectors.toList());
        calendar = calendars.get(0); // first block calender entry
        event = calendar.getComponent("VEVENT");

        System.out.println(event);
    }

    @Test
    public void shouldReturnCasSchedulerAsUid() {
        String uid = event.getProperty("UID").getValue();

        assertEquals("cas-scheduler", uid);
    }

    @Test
    public void shouldReturnCorrectProdId() {
        String prodId = calendar.getProperty("PRODID").getValue();

        System.out.println(calendar);

        assertEquals(prodId, "-//CAS Scheduler//iCal4j 2.0//EN");
    }

    @Test
    public void shouldReturnNameAsSummaryWithBlockInformation() {
        String summary = event.getProperty("SUMMARY").getValue();

        assertEquals("Angewandte Ingenieurmathematik_1-Block", summary);
    }

    @Test
    public void shouldReturnCorrectEnddate() {
        String end = event.getProperty("DTEND").getValue();

        // the end date of iCal is exclusive and must thus be +1 of the actual end
        String expected = LocalDate.of(2020, 9, 29).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        assertEquals(expected, end);
    }

    @Test
    public void shouldReturnCorrectStartdate() {
        String start = event.getProperty("DTSTART").getValue();

        String expected = START.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        assertEquals(expected, start);
    }

    @Test
    public void shouldRecognizeNextMonth() {
        dao = LectureDao.builder()
                .lectureCode("T3M10010")
                .name("Angewandte Ingenieurmathematik")
                .blocks(Arrays.asList(
                        BlockDao.builder()
                                .blockStart(START)
                                .blockEnd(LocalDate.of(2020, 8, 31))
                                .location("MA")
                                .build(),
                        BlockDao.builder()
                                .blockStart(START.plusDays(1))
                                .blockEnd(END.plusDays(1))
                                .location("MA")
                                .build()
                ))
                .build();

        List<IcsCalendarWrapper> wrappers = icsCreatorService.create(dao);
        calendars = wrappers.stream().map(wrapper -> wrapper.getCalendar()).collect(Collectors.toList());
        calendar = calendars.get(0);
        event = calendar.getComponent("VEVENT");

        System.out.println(event);

        String emd = event.getProperty("DTEND").getValue();

        String expected = LocalDate.of(2020, 9, 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        assertEquals(expected, emd);
    }

}
