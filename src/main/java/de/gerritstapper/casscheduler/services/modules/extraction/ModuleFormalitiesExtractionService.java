package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.FormalitiesInformation;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.FORMALITIES_HEADLINE;

@Service
@Log4j2
public class ModuleFormalitiesExtractionService implements IExtractionHelper {

    private static final String NOT_AVAILABLE = "N/A";

    public boolean isFormalitiesHeadline(String line) {
        return matchesHeadlineIgnoreCase(line, FORMALITIES_HEADLINE);
    }

    public FormalitiesInformation extractFormalitiesInformation(String content) {
        log.debug("extractFormalities(): {}", content);

        String[] formalitiesContent = content.split(ExtractionPatterns.WHITESPACE.getPattern());

        String lectureCode = formalitiesContent[0];
        String duration = formalitiesContent[2];
        String owner = extractOwner(formalitiesContent);
        String language = includesLanguageInformation(formalitiesContent) ? formalitiesContent[formalitiesContent.length - 1] : NOT_AVAILABLE;

        return FormalitiesInformation.builder()
                .lectureCode(lectureCode)
                .duration(duration)
                .owner(owner)
                .language(language)
                .build();
    }

    private String extractOwner(String[] formalities) {
        log.debug("extractOwner(): {}", formalities);

        int lastInfoIndex = includesLanguageInformation(formalities) ? formalities.length - 1 : formalities.length;
        int ownerStartIndex = 3;

        StringBuilder output = new StringBuilder();

        List<String> lecturerParts = java.util.Arrays.stream(formalities, ownerStartIndex, lastInfoIndex)
                .collect(Collectors.toList());

        for (String part : lecturerParts) {
            output.append(part).append(" ");
        }

        return output.toString().trim();
    }

    private boolean includesLanguageInformation(String[] formalities) {
        String possibleLanguageInformation = formalities[formalities.length -1];

        return possibleLanguageInformation.contains("Deutsch") || possibleLanguageInformation.contains("Englisch");
    }
}
