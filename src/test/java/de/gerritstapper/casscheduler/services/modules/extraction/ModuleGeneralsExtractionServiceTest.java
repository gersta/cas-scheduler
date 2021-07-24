package de.gerritstapper.casscheduler.services.modules.extraction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleGeneralsExtractionServiceTest {

    private ModuleGeneralsExtractionService extractionService;

    @BeforeEach
    void beforeEach() {
        extractionService = new ModuleGeneralsExtractionService();
    }

    @Test
    void shouldReturnLectureNameWithoutLectureCode() {
        String line = "Rechtliche Rahmenbedingungen unternehmerischer Entscheidungen (W3M40008)";

        String result = extractionService.extractLectureName(line);

        assertEquals("Rechtliche Rahmenbedingungen unternehmerischer Entscheidungen", result);
    }

    @Test
    void shouldReturnLectureName() {
        String line = "Volkswirtschaftliche Rahmenbedingungen unternehmerischer Entscheidungen ";

        String result = extractionService.extractLectureName(line);

        assertEquals("Volkswirtschaftliche Rahmenbedingungen unternehmerischer Entscheidungen", result);
    }

    @Test
    void shouldTrimWhitespacesAtBeginningAndEnd() {
        String line = " Volkswirtschaftliche Rahmenbedingungen ";

        String result = extractionService.extractLectureName(line);

        assertEquals("Volkswirtschaftliche Rahmenbedingungen", result);
    }

}