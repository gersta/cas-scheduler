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
        String[] lines = { "AUS AKTUELLER ORGA-EINHEIT" };

        assertTrue(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextInLowercase() {
        String[] lines = { "aus aktueller orga-einheit" };

        assertTrue(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextInMixedCase() {
        String[] lines = { "aus AKTUELLER orga-EINHEIT" };

        assertTrue(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineNewModuleByExpectedTextWithoutWhitespaces() {
        String[] lines = { "AUSAKTUELLERORGA-EINHEIT" };

        assertTrue(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineExistingModuleByUnexpectedText() {
        String[] lines = { "ANY TEXT" };

        assertFalse(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineExistingModuleByEmptyTest() {
        String[] lines = { "" };

        assertFalse(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineExistingModuleByNull() {
        String[] lines = { null };

        assertFalse(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineExistingModuleByNullLines() {
        String[] lines = null;

        assertFalse(fieldExtractorService.isNewModule(lines));
    }

    @Test
    void shouldDetermineExistingModuleByEmptyLines() {
        String[] lines = {};

        assertFalse(fieldExtractorService.isNewModule(lines));
    }
}