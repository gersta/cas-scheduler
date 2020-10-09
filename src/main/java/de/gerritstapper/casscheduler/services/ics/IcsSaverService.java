package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.util.CalenderHelperUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.FileOutputStream;
import java.io.IOException;

public class IcsSaverService {

    public static Calendar saveFile(Calendar calendar, String outputDir) {
        VEvent event = CalenderHelperUtil.getVEvent(calendar);
        String summary = CalenderHelperUtil.getSummaryAsString(event).replace(" ", "_");
        String start = CalenderHelperUtil.getStartAsString(event);
        String location = CalenderHelperUtil.getLocationAsString(event);

        try {
            FileOutputStream fileOut = new FileOutputStream(outputDir + summary + "_start_" + start + "_location_" + location + ".ics");
            CalendarOutputter output = new CalendarOutputter();
            output.output(calendar, fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}
