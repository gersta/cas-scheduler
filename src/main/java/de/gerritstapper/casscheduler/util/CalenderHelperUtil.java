package de.gerritstapper.casscheduler.util;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.springframework.stereotype.Service;

@Service
public class CalenderHelperUtil {

    public static final String VEVENT = "VEVENT";

    public VEvent getVEvent(Calendar calendar) {
        return (VEvent) calendar.getComponent(VEVENT);
    }

    public String getSummaryAsString(VEvent event) {
        return event.getSummary().getValue();
    }

    public String getStartAsString(VEvent event) {
        return event.getStartDate().getValue();
    }

    public String getLocationAsString(VEvent event) {
        return event.getLocation().getValue();
    }
}
