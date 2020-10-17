package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.util.CalenderHelperUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class IcsSaverService {

    private final CalenderHelperUtil calenderHelperUtil;
    private final String OUTPUT_DIRECTORY;

    public IcsSaverService(
            @Value("${cas-scheduler.ics.output}") String icsOutputDirectory,
            final CalenderHelperUtil calenderHelperUtil
    ) {
        this.OUTPUT_DIRECTORY = icsOutputDirectory;

        this.calenderHelperUtil = calenderHelperUtil;
    }

    public void saveFile(Calendar calendar) {
        VEvent event = calenderHelperUtil.getVEvent(calendar);
        String name = calenderHelperUtil.getSummaryAsString(event).replace(" ", "_");
        String start = calenderHelperUtil.getStartAsString(event);
        String location = calenderHelperUtil.getLocationAsString(event);

        try {
            String filename = OUTPUT_DIRECTORY + name + "_start_" + start + "_location_" + location + ".ics";
            FileOutputStream fileOut = new FileOutputStream(filename);
            CalendarOutputter output = new CalendarOutputter();
            output.output(calendar, fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
