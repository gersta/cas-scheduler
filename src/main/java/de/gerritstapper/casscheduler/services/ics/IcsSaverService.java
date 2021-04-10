package de.gerritstapper.casscheduler.services.ics;

import de.gerritstapper.casscheduler.models.IcsCalendarWrapper;
import lombok.extern.log4j.Log4j2;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Log4j2
public class IcsSaverService {

    private final String OUTPUT_DIRECTORY;

    public IcsSaverService(
            @Value("${cas-scheduler.ics.output}") String icsOutputDirectory
    ) {
        this.OUTPUT_DIRECTORY = icsOutputDirectory;
    }

    public void saveFile(IcsCalendarWrapper calendarWrapper) {
       log.debug("saveFile(): {}", calendarWrapper.getFilename());

        Calendar calendar = calendarWrapper.getCalendar();

        try {
            String filename = OUTPUT_DIRECTORY + calendarWrapper.getFilename();
            log.debug("Writing ics file to {}", filename);

            FileOutputStream fileOut = new FileOutputStream(filename);
            CalendarOutputter output = new CalendarOutputter();
            output.output(calendar, fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
