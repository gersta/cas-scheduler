package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ModulePagesGroupingServiceTest {

    private ModulePagesGroupingService groupingService;
    private PDDocument document;

    @BeforeEach
    void beforeEach() throws IOException {
        String filename = "M_T_Modulhandbuch_Grouping.pdf";

        document = PDDocument.load(new File("src/test/resources/" + filename));

        ModulePdfTextStripper textStripper = Mockito.mock(ModulePdfTextStripper.class);

        // return empty text for all pages...
        when(textStripper.getTextForPage(anyInt())).thenReturn("");
        // ... except for the first and third, which act as the markings for new modules
        when(textStripper.getTextForPage(1)).thenReturn("ausaktuellerorga-einheit"); // TODO: make this string globally available
        when(textStripper.getTextForPage(3)).thenReturn("ausaktuellerorga-einheit"); // TODO: make this string globally available

        groupingService = new ModulePagesGroupingService(textStripper, new ModulesFieldExtractorService());
    }

    @Test
    void shouldGroupTwoPagesForFirstModule() throws IOException {
        Map<Integer, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule(document.getPages());

        List<ModulePdfPage> pages = result.get(1);

        assertEquals(2, pages.size());
    }

    @Test
    void shouldGroupThreePagesForSecondModule() throws IOException {
        Map<Integer, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule(document.getPages());

        List<ModulePdfPage> pages = result.get(2);

        assertEquals(2, pages.size());
    }

    @Test
    void shouldMapEachPdPageToItsIndexInDocument() throws IOException {
        Map<Integer, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule(document.getPages());

        assertTrue(
                result.values().stream()
                        .flatMap(Collection::stream)
                        .allMatch(page -> page.getPageIndexInDocument() > 0)
        );
    }

}