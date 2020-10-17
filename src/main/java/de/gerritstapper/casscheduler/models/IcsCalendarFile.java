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
public class IcsCalendarFile {

    String filename;
    Calendar calendar;
}
