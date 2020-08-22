package de.gerritstapper.casscheduler.models;

import lombok.Data;
import org.apache.pdfbox.pdmodel.PDPage;

@Data
public class PageTuple {

    private final PDPage page;
    private final double offset;
}
