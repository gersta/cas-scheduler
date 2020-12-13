package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.models.DatesTuple;
import de.gerritstapper.casscheduler.services.persistence.DataProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessServiceTest {

    private DataProcessService dataProcessService;

    @BeforeEach
    void beforeEach() {
        String datePattern = "dd.MM.yyyy";
        dataProcessService = new DataProcessService(datePattern);
    }

    @Test
    public void shouldAppendYearForStart() {
        DatesTuple<LocalDate, LocalDate> dates = dataProcessService.getDates("29.08.", "30.08.2022");

        assertEquals(LocalDate.of(2022, 8, 29), dates.getStart());
    }
}
