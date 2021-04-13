package de.gerritstapper.casscheduler.services.modules.pdf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModulesFieldExtractorServiceTest {

    private ModulesFieldExtractorService fieldExtractorService;

    @BeforeEach
    void beforeEach() {
        fieldExtractorService = new ModulesFieldExtractorService();
    }


    @Test
    void shouldDetermineNewModuleByExpectedText() {
        String content = "AUS AKTUELLER ORGA-EINHEIT";

        assertTrue(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextInLowercase() {
        String content = "aus aktueller orga-einheit";

        assertTrue(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextInMixedCase() {
        String content = "aus AKTUELLER orga-EINHEIT";

        assertTrue(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextWithoutWhitespaces() {
        String content = "AUSAKTUELLERORGA-EINHEIT";

        assertTrue(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineExistingModuleByUnexpectedText() {
        String content = "ANY TEXT";

        assertFalse(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineExistingModuleByEmptyTest() {
        String content = "";

        assertFalse(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineExistingModuleByNull() {
        String content = null;

        assertFalse(fieldExtractorService.isNewModule(content));
    }

    @Test
    void shouldDetermineExistingModuleByNullLines() {
        String content = null;

        assertFalse(fieldExtractorService.isNewModule(content));
    }
}