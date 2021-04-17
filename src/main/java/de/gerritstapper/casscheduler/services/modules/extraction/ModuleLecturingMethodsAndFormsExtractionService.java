package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.LecturingFormsInformation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.LECTURING_FORMS_METHODS_HEADLINE;

@Service
@Log4j2
public class ModuleLecturingMethodsAndFormsExtractionService implements IExtractionHelper {

    public boolean isLecturingMethods(String line) {
        return matchesHeadlineLowercase(line, LECTURING_FORMS_METHODS_HEADLINE);
    }

    public LecturingFormsInformation extractLecturingFormsAndMethods(String lecturingMethodsLine) {
        log.debug("extractLecturingFormsAndMethods(): {}", lecturingMethodsLine);

        String[] methodsContent = lecturingMethodsLine.split("(?<=\\w) (?=\\w)");

        String forms = methodsContent[0].trim();
        String methods = methodsContent[1].trim();

        return LecturingFormsInformation.builder()
                .lecturingForms(forms)
                .lecturingMethods(methods)
                .build();
    }
}
