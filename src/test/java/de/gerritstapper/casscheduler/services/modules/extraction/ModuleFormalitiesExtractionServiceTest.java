package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.FormalitiesInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleFormalitiesExtractionServiceTest {

    private ModuleFormalitiesExtractionService formalitiesExtractionService;

    @BeforeEach
    void beforeEach() {
        formalitiesExtractionService = new ModuleFormalitiesExtractionService();
    }

    @Test
    void shouldExtractOwnerFromFormalities() {
        String content = "W3M20033 - 1 Prof. Dr.-Ing. Clemens Martin Deutsch/Englisch";

        FormalitiesInformation result = formalitiesExtractionService.extractFormalitiesInformation(content);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Prof. Dr.-Ing. Clemens Martin", result.getOwner())
        );
    }

    /**
     * Masterarbeit (W3M20040)
     */
    @Test
    void shouldExtractOwnerOnModulesWithoutLanguageInformation() {
        String content = "W3M20040 - 1 Prof. Dr.-Ing. Clemens Martin";

        FormalitiesInformation result = formalitiesExtractionService.extractFormalitiesInformation(content);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Prof. Dr.-Ing. Clemens Martin", result.getOwner())
        );
    }

    @Test
    void shouldSetLanguageToNotAvailableIfMissing() {
        String content = "W3M20040 - 1 Prof. Dr.-Ing. Clemens Martin";

        FormalitiesInformation result = formalitiesExtractionService.extractFormalitiesInformation(content);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("N/A", result.getLanguage())
        );
    }

}