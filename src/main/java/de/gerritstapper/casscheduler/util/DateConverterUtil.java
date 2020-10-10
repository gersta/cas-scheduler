package de.gerritstapper.casscheduler.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateConverterUtil {

    public java.util.Date fromLocalDateToUtilDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    public net.fortuna.ical4j.model.Date fromLocalDateToIcalDate(LocalDate date) {
         return new net.fortuna.ical4j.model.Date(fromLocalDateToUtilDate(date));
    }
}
