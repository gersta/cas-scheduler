package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines;

public interface IExtractionHelper {

    default boolean matchesHeadlineIgnoreCase(String line, ExtractionHeadlines headline) {
        return line.equalsIgnoreCase(headline.getHeadline());
    }
}
