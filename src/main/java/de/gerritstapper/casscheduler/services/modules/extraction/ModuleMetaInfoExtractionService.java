package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.MetaInformation;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.METAINFO_HEADLINE;

@Service
@Log4j2
public class ModuleMetaInfoExtractionService implements IExtractionHelper {

    public boolean isMetaInfo(String line) {
        return matchesHeadlineLowercase(line, METAINFO_HEADLINE);
    }

    public MetaInformation extractMetainformation(String metaInfoLine) {
        log.debug("extractMetainformation(): {}", metaInfoLine);

        String[] metainfo = metaInfoLine.split(ExtractionPatterns.WHITESPACE.getPattern());
        String updatedOn = metainfo[2];

        return MetaInformation.builder()
                .updatedOn(updatedOn)
                .build();
    }
}
