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

    public boolean isFormalitiesHeadline(String line) {
        return matchesHeadlineLowercase(line, FORMALITIES_HEADLINE);
    }

    public FormalitiesInformation extractFormalitiesInformation(String content) {
        log.debug("extractFormalities(): {}", content);

        String[] formalitiesContent = content.split(ExtractionPatterns.WHITESPACE.getPattern());

        String lectureCode = formalitiesContent[0];
        String duration = formalitiesContent[2];
        String owner = extractOwner(formalitiesContent);
        String language = formalitiesContent[formalitiesContent.length - 1];

        return FormalitiesInformation.builder()
                .lectureCode(lectureCode)
                .duration(duration)
                .owner(owner)
                .language(language)
                .build();
    }

    private String extractOwner(String[] formalities) {
        log.debug("extractOwner(): {}", formalities);

        int last = formalities.length - 1;
        int start = 3;

        StringBuilder output = new StringBuilder();

        List<String> lecturerParts = java.util.Arrays.stream(formalities, start, last)
                .collect(Collectors.toList());

        for (String part : lecturerParts) {
            output.append(part).append(" ");
        }

        return output.toString().trim();
    }
}
