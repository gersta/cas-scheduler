package de.gerritstapper.casscheduler.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.Calendar;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * Convinience class to combine a ICS {@link Calendar} with the filename
 * associated to it when writting to disk
 * This makes it easier to trcck the events/calenders data with its later
 * access point, the filename
 */
public class IcsCalendarWrapper {

    private Calendar calendar;
    private String filename;
}
