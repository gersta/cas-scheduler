package de.gerritstapper.casscheduler.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDPage;

@Data
@AllArgsConstructor
public class DatesTuple<A, B> {

    private A start;
    private B end;
}
