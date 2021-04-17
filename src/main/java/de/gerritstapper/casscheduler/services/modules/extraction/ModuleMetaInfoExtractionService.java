package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.MetaInformation;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.METAINFO_HEADLINE;

@Service
@Log4j2
public class ModuleMetaInfoExtractionService {

    public boolean isMetaInfo(String line) {
        return startsWithHeadlineIgnoreCase(line, METAINFO_HEADLINE);
    }

    public MetaInformation extractMetainformation(String metaInfoLine) {
        log.debug("extractMetainformation(): {}", metaInfoLine);

        String[] metainfo = metaInfoLine.split(ExtractionPatterns.WHITESPACE.getPattern());

        String updatedOn = metainfo[2];

        return MetaInformation.builder()
                .updatedOn(updatedOn)
                .build();
    }

    private boolean startsWithHeadlineIgnoreCase(String line, ExtractionHeadlines headline) {
        return line.toLowerCase().startsWith(headline.getHeadline().toLowerCase());
    }
}
