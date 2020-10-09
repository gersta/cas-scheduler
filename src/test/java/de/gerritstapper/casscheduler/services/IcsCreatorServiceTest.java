package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.services.ics.IcsCreatorService;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IcsCreatorServiceTest {

    private LectureDao dao;

    private Calendar calendar;
    private CalendarComponent event;

    private final LocalDate START = LocalDate.of(2020, 9, 27);
    private final LocalDate END = LocalDate.of(2020, 9, 28);


    @BeforeEach
    void beforeEach() {
        dao = LectureDao.builder()
                            .id("T3M10010")
                            .name("Angewandte Ingenieurmathematik")
                            .startDate(START)
                            .endDate(END)
                            .build();

        calendar = IcsCreatorService.create(dao);
        event = calendar.getComponent("VEVENT");

        System.out.println(event);
    }

    @Test
    public void shouldReturnNameAsSummary() {
        String summary = event.getProperty("SUMMARY").getValue();

        assertEquals("Angewandte Ingenieurmathematik", summary);
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
                .startDate(START)
                .endDate(LocalDate.of(2020, 8, 31))
                .build();

        calendar = IcsCreatorService.create(dao);
        event = calendar.getComponent("VEVENT");

        System.out.println(event);

        String emd = event.getProperty("DTEND").getValue();

        String expected = LocalDate.of(2020, 9, 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        assertEquals(expected, emd);
    }


}
