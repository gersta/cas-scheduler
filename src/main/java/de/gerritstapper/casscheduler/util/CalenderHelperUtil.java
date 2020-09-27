package de.gerritstapper.casscheduler.util;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

public class CalenderHelperUtil {

    public static final String VEVENT = "VEVENT";

    public static VEvent getVEvent(Calendar calendar) {
        return (VEvent) calendar.getComponent(VEVENT);
    }

    public static String getSummaryAsString(VEvent event) {
        return event.getSummary().getValue();
    }

    public static String getStartAsString(VEvent event) {
        return event.getStartDate().getValue();
    }

    public static String getLocationAsString(VEvent event) {
        return event.getLocation().getValue();
    }
}
