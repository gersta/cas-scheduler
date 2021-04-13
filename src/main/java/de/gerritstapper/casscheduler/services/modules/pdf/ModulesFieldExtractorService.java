package de.gerritstapper.casscheduler.services.modules.pdf;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class ModulesFieldExtractorService {

    private static final String NEW_PAGE_TEXT = "ausaktuellerorga-einheit";
    private static final String WHITESPACE = " ";

    public boolean isNewModule(String pageContent) {
        if (Objects.nonNull(pageContent) ) {

            String lowercase = pageContent.toLowerCase();
            String withoutWhitespace = lowercase.replaceAll(WHITESPACE, "");

            return withoutWhitespace.startsWith(NEW_PAGE_TEXT);
        }

        return false;
    }
}
