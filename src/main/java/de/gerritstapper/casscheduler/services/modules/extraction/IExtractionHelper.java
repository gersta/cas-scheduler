package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines;

public interface IExtractionHelper {

    default boolean matchesHeadlineLowercase(String line, ExtractionHeadlines headline) {
        return line.toLowerCase().startsWith(headline.getHeadline().toLowerCase());
    }
}
