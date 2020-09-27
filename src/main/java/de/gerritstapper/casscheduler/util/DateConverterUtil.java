package de.gerritstapper.casscheduler.util;

import java.time.LocalDate;

public class DateConverterUtil {

    public static java.util.Date fromLocalDateToUtilDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    public static net.fortuna.ical4j.model.Date fromLocalDateToIcalDate(LocalDate date) {
         return new net.fortuna.ical4j.model.Date(fromLocalDateToUtilDate(date));
    }
}
