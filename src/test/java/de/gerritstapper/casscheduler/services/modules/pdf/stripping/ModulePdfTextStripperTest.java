package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.module.CasPdPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ModulePdfTextStripperTest {

    private ModulePdfTextStripper textStripper;

    @BeforeEach
    void beforeEach() throws IOException {
        String filename = "M_T_Modulhandbuch_Grouping.pdf";

        textStripper = new ModulePdfTextStripper(filename, new ModuleDataCleansingService());
    }

    @Test
    void shouldReturnTextForPageOne() {
        String pageOneTextStart = "AUS AKTUELLER ORGA-EINHEIT";

        CasPdPage result = textStripper.getPdfPages().get(0);

        assertTrue(result.getPageContent().startsWith(pageOneTextStart));
    }

    @Test
    void shouldReturnTextForPageTwo() {
        String pageTwoTextStart = "LERNEINHEITEN UND INHALTE";

        CasPdPage result = textStripper.getPdfPages().get(1);

        assertTrue(result.getPageContent().startsWith(pageTwoTextStart));
    }

    @Test
    void shouldConvertPdPageTreeOfInputDocumentIntoListOfPages() {
        List<CasPdPage> pages = textStripper.getPdfPages();

        assertEquals(5, pages.size());
    }

    @Test
    void shouldReturnPageContentForEachPageOfTheDocument() {
        List<CasPdPage> pages = textStripper.getPdfPages();

        long numberOfPagesWithContentOrUpdateTimestamp = pages.stream()
                .filter(page -> Objects.nonNull(page.getPageContent()))
                .filter(page -> page.getPageContent().contains("Stand vom 13.07.2020"))
                .count();

        assertAll(
                () -> assertEquals(5, pages.size()),
                () -> assertEquals(5, numberOfPagesWithContentOrUpdateTimestamp)
        );
    }

}