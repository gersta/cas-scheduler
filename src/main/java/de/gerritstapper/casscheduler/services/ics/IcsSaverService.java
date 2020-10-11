package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.util.CalenderHelperUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class IcsSaverService {

    private final CalenderHelperUtil calenderHelperUtil;

    public IcsSaverService(
            final CalenderHelperUtil calenderHelperUtil
    ) {
        this.calenderHelperUtil = calenderHelperUtil;
    }

    public void saveFile(Calendar calendar, String outputDir) {
        VEvent event = calenderHelperUtil.getVEvent(calendar);
        String summary = calenderHelperUtil.getSummaryAsString(event).replace(" ", "_");
        String start = calenderHelperUtil.getStartAsString(event);
        String location = calenderHelperUtil.getLocationAsString(event);

        try {
            FileOutputStream fileOut = new FileOutputStream(outputDir + summary + "_start_" + start + "_location_" + location + ".ics");
            CalendarOutputter output = new CalendarOutputter();
            output.output(calendar, fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
