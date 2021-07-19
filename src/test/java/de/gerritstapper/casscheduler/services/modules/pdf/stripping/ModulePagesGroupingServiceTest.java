package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.module.CasPdPage;
import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import de.gerritstapper.casscheduler.services.modules.pdf.ModuleDataCleansingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePagesGroupingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePdfTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ModulePagesGroupingServiceTest {

    private ModulePagesGroupingService groupingService;

    @BeforeEach
    void beforeEach() {
        ModulePdfTextStripper textStripper = Mockito.mock(ModulePdfTextStripper.class);

        PDPage dummyPage = new PDPage();
        List<CasPdPage> pdfPages = Arrays.asList(
                CasPdPage.builder()
                        .page(dummyPage)
                        .pageContent("""
                                Embedded Systems im Kraftfahrzeug (T3M10506)Automotive Embedded Systems
                                Stand vom 13.07.2020 T3M10506 // Seite 72
                                """)
                        .build(),
                CasPdPage.builder()
                        .page(dummyPage)
                        .pageContent("""
                                LERNEINHEITEN UND INHALTE
                                Stand vom 13.07.2020 T3M10506 // Seite 73
                                """)
                        .build(),
                CasPdPage.builder()
                        .page(dummyPage)
                        .pageContent("""
                                Verbrennungsmotoren (Grundlagen) (T3M10507)Combustion Engines (Basics)
                                Stand vom 13.07.2020 T3M10507 // Seite 74
                                """)
                        .build(),
                CasPdPage.builder()
                        .page(dummyPage)
                        .pageContent("""
                                QUALIFIKATIONSZIELE UND KOMPETENZEN
                                INFORMATION TO ANY OTHER LECTURE: T3M10506
                                Stand vom 13.07.2020 T3M10507 // Seite 75
                                """)
                        .build()
        );

        when(textStripper.getPdfPages()).thenReturn(pdfPages);

        groupingService = new ModulePagesGroupingService(
                textStripper
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

        assertEquals(4, numberOfPages);
    }

    @Test
    void shouldGroupTwoPagesForFirstModule() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        List<ModulePdfPage> pages = result.get("T3M10506");

        assertEquals(2, pages.size());
    }

    @Test
    void shouldGroupTwoPagesForSecondModule() {
        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        List<ModulePdfPage> pages = result.get("T3M10507");

        assertEquals(2, pages.size());
    }

    @Test
    void shouldIgnoreLectureCodesWhichAreNotPartOfTheLastLineOfThePageContent() {
        List<CasPdPage> pagesWithMultipleLectureCodes = Arrays.asList(
                CasPdPage.builder()
                        .page(new PDPage())
                        .pageContent("""
                                Verbrennungsmotoren (Grundlagen) (T3M10507)Combustion Engines (Basics)
                                Stand vom 13.07.2020 T3M10507 // Seite 74
                                """)
                        .build(),
                CasPdPage.builder()
                        .page(new PDPage())
                        .pageContent("""
                                QUALIFIKATIONSZIELE UND KOMPETENZEN
                                INFORMATION TO ANY OTHER LECTURE: T3M10506
                                Stand vom 13.07.2020 T3M10507 // Seite 75
                                """)
                        .build()
        );

        ModulePdfTextStripper textStripperMock = Mockito.mock(ModulePdfTextStripper.class);
        when(textStripperMock.getPdfPages()).thenReturn(pagesWithMultipleLectureCodes);

        ModulePagesGroupingService groupingService = new ModulePagesGroupingService(
                textStripperMock
        );

        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        assertAll(
                () -> assertEquals(1, result.keySet().size()),
                () -> assertEquals(2, result.get("T3M10507").size()),
                () -> assertNull(result.get("T3M10506"))
        );
    }

    @Test
    void shoulReturnEmptyStringForMissingLectureCode() {
        List<CasPdPage> pageWithoutLectureCode = Collections.singletonList(
                CasPdPage.builder()
                        .page(new PDPage())
                        .pageContent("""
                                Verbrennungsmotoren (Grundlagen) Combustion Engines (Basics)
                                Stand vom 13.07.2020 // Seite 74
                                """)
                        .build()
        );

        ModulePdfTextStripper textStripperMock = Mockito.mock(ModulePdfTextStripper.class);
        when(textStripperMock.getPdfPages()).thenReturn(pageWithoutLectureCode);

        ModulePagesGroupingService groupingService = new ModulePagesGroupingService(
                textStripperMock
        );

        Map<String, List<ModulePdfPage>> result = groupingService.groupPdfPagesByModule();

        assertAll(
                () -> assertEquals(1, result.keySet().size()),
                () -> assertEquals(1, result.get("").size())
        );
    }

}