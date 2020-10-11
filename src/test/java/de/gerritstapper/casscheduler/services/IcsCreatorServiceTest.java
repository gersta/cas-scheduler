package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.services.ics.IcsCreatorService;
import de.gerritstapper.casscheduler.util.DateConverterUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        icsCreatorService = new IcsCreatorService(new DateConverterUtil());

        dao = LectureDao.builder()
                            .id("T3M10010")
                            .name("Angewandte Ingenieurmathematik")
                            .firstBlockStart(START)
                            .firstBlockEnd(END)
                            .secondBlockStart(START.plusDays(1))
                            .secondBlockEnd(END.plusDays(1))
                            .build();

        calendars = icsCreatorService.create(dao);
        calendar = calendars.get(0); // first block calender entry
        event = calendar.getComponent("VEVENT");

        System.out.println(event);
    }

    @Test
    public void shouldIncludeNameInSummary() {
        String summary = event.getProperty("SUMMARY").getValue();

        assertTrue(summary.contains("Angewandte Ingenieurmathematik"));
    }

    @Test
    public void shouldContainBlockInformationInSummary() {
        String summary = event.getProperty("SUMMARY").getValue();

        assertTrue(summary.contains("1st Block"));
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
                .id("T3M10010")
                .name("Angewandte Ingenieurmathematik")
                .firstBlockStart(START)
                .firstBlockEnd(LocalDate.of(2020, 8, 31))
                .secondBlockStart(START.plusDays(1))
                .secondBlockEnd(END.plusDays(1))
                .build();

        calendars = icsCreatorService.create(dao);
        calendar = calendars.get(0);
        event = calendar.getComponent("VEVENT");

        System.out.println(event);

        String emd = event.getProperty("DTEND").getValue();

        String expected = LocalDate.of(2020, 9, 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        assertEquals(expected, emd);
    }

}
