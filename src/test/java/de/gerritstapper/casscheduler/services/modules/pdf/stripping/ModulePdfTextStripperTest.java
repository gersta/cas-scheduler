package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePdfTextStripper;
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

    @Test
    void shouldReturnLectureCodeForPageIndex() {
        String pageOneLectureCode = textStripper.getLectureCodeForPage(1);
        String pageTwoLectureCode = textStripper.getLectureCodeForPage(2);
        String pageThreeLectureCode = textStripper.getLectureCodeForPage(3);
        String pageFourLectureCode = textStripper.getLectureCodeForPage(4);
        String pageFiveLectureCode = textStripper.getLectureCodeForPage(5);

        assertAll(
                () -> assertEquals("T3M10506", pageOneLectureCode),
                () -> assertEquals("T3M10506", pageTwoLectureCode),
                () -> assertEquals("T3M10507", pageThreeLectureCode),
                () -> assertEquals("T3M10507", pageFourLectureCode),
                () -> assertEquals("T3M10507", pageFiveLectureCode)
        );
    }

    @Test
    void shouldReturnLectueCodeFromPageForMasterThesis() throws IOException {
        String filename = "M_T_Modulhandbuch_Master_Thesis.pdf";

        textStripper = new ModulePdfTextStripper(filename);

        String result = textStripper.getLectureCodeForPage(1);

        assertEquals("T3MX0202", result);
    }

    @Test
    void shoulReturnEmptyStringForMissingLectureCode() throws IOException {
        String filename = "M_T_Modulhandbuch_Non_Module.pdf";

        textStripper = new ModulePdfTextStripper(filename);

        String result = textStripper.getLectureCodeForPage(1);

        assertEquals("", result);
    }

}