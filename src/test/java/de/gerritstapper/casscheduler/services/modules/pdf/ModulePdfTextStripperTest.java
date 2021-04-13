package de.gerritstapper.casscheduler.services.modules.pdf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ModulePdfTextStripperTest {

    private ModulePdfTextStripper textStripper;

    @BeforeEach
    void beforeEach() throws IOException {
        String filename = "M_T_Modulhandbuch_Grouping.pdf";

        textStripper = new ModulePdfTextStripper(filename);
    }

    @Test
    void shouldReturnTextFromPageOneForIndexOne() {
        String pageOneTextStart = "AUS AKTUELLER ORGA-EINHEIT";

        String result = textStripper.getTextForPage(1);

        assertTrue(result.startsWith(pageOneTextStart));
    }

    @Test
    void shouldReturnTextFromPageOneForIndexTwo() {
        String pageTwoTextStart = "LERNEINHEITEN UND INHALTE";

        String result = textStripper.getTextForPage(2);

        assertTrue(result.startsWith(pageTwoTextStart));
    }

    @Test
    void shouldReturnEmptyTextForIndexOutOfBounds() {
        String emptyText = "";

        String result = textStripper.getTextForPage(1000);

        assertEquals(emptyText, result);
    }

}