package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import de.gerritstapper.casscheduler.services.modules.pdf.ModuleDataCleansingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePagesGroupingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePdfTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ModulePagesGroupingServiceTest {

    private static final String FIRST_MODULE = "T3M10506";
    private static final String SECOND_MODULE = "T3M10507";

    private ModulePagesGroupingService groupingService;

    @BeforeEach
    void beforeEach() throws IOException {
        String filename = "M_T_Modulhandbuch_Grouping.pdf";

        PDDocument document = PDDocument.load(new File("src/test/resources/" + filename));

        ModulePdfTextStripper textStripper = Mockito.mock(ModulePdfTextStripper.class);

        when(textStripper.getPdfPages()).thenReturn(document.getPages());
        when(textStripper.getTextForPage(anyInt())).thenReturn("");

        when(textStripper.getLectureCodeForPage(1)).thenReturn("T3M10506");
        when(textStripper.getLectureCodeForPage(2)).thenReturn("T3M10506");
        when(textStripper.getLectureCodeForPage(3)).thenReturn("T3M10507");
        when(textStripper.getLectureCodeForPage(4)).thenReturn("T3M10507");
        when(textStripper.getLectureCodeForPage(5)).thenReturn("T3M10507");

        groupingService = new ModulePagesGroupingService(
                textStripper,
                new ModuleDataCleansingService()
        );
    }

    @Test
    void shouldCreateTwoModuleGroups() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        int modules = result.keySet().size();

        assertEquals(2, modules);
    }

    @Test
    void shouldGroupAllPagesFromTheDocument() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        List<ModulePdfPage> totalPages = result.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        int numberOfPages = totalPages.size();

        assertEquals(5, numberOfPages);
    }

    @Test
    void shouldGroupTwoPagesForFirstModule() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        List<ModulePdfPage> pages = result.get(FIRST_MODULE);

        assertEquals(2, pages.size());
    }

    @Test
    void shouldGroupThreePagesForSecondModule() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        List<ModulePdfPage> pages = result.get(SECOND_MODULE);

        assertEquals(3, pages.size());
    }

    @Test
    void shouldMapEachPdPageToItsIndexInDocument() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        assertTrue(
                result.values().stream()
                        .flatMap(Collection::stream)
                        .allMatch(page -> page.getPageIndexInDocument() > 0)
        );
    }

}