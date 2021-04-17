package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.services.modules.pdf.ModulePdfTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ModuleExtractionManagerTest {

    private ModuleExtractionManager extractionManager;
    private String pageContent;

    @BeforeEach
    void beforeEach() throws IOException {
        ModuleExamExtractionService examExtractionService = new ModuleExamExtractionService();
        ModuleFormalitiesExtractionService formalitiesExtractionService = new ModuleFormalitiesExtractionService();
        ModuleGeneralsExtractionService generalsExtractionService = new ModuleGeneralsExtractionService();
        ModuleLecturingMethodsAndFormsExtractionService lecturingMethodsAndFormsExtractionService = new ModuleLecturingMethodsAndFormsExtractionService();
        ModuleMetaInfoExtractionService metaInfoExtractionService = new ModuleMetaInfoExtractionService();
        ModuleWorkloadExtractionService workloadExtractionService = new ModuleWorkloadExtractionService();

        extractionManager = new ModuleExtractionManager(
                generalsExtractionService,
                formalitiesExtractionService,
                lecturingMethodsAndFormsExtractionService,
                examExtractionService,
                workloadExtractionService,
                metaInfoExtractionService
        );

        // Instead of relying on the extraction logic of a pdf, take the pages content from a text file
        Path filename = Path.of("src/test/resources/M_T_Modulhandbuch_T3M10101.txt");
        pageContent = Files.readString(filename);
    }

    @Test
    void shouldExtractLectureCode() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("T3M10101", module.getLectureCode())
        );
    }

    @Test
    void shouldExtractLectureName() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Angewandte Ingenieurmathematik", module.getLectureName())
        );
    }

    @Test
    void shouldExtractLectureNameEnglish() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Applied Engineering Mathematics", module.getLectureNameEnglish())
        );
    }

    @Test
    void shouldExtractOwner() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Prof. Dr. Volker Schulz", module.getOwner())
        );
    }

    @Test
    void shouldExtractDuration() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("1", module.getDuration())
        );
    }

    @Test
    void shouldExtractLanguage() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Deutsch/Englisch", module.getLanguage())
        );
    }

    @Test
    void shouldExtractExam() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("Klausur", module.getExam())
        );
    }

    @Test
    void shouldExtractExamDuration() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("120", module.getExamDuration())
        );
    }

    @Test
    void shouldExtractExamMarking() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("ja", module.getExamMarking())
        );
    }

    @Test
    void shouldExtractTotalWorkload() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("150", module.getTotalWorkload())
        );
    }

    @Test
    void shouldExtractPresentWorkload() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("50", module.getPresentWorkload())
        );
    }

    @Test
    void shouldExtractSelfstudyWorkload() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("100", module.getSelfStudyWorkload())
        );
    }

    @Test
    void shouldExtractEctsPoints() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("5", module.getEctsPoints())
        );
    }

    @Test
    void shouldExtractUpdatedOn() {
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals("13.07.2020", module.getUpdatedOn())
        );
    }

    @Test
    void shouldExtractLecturingForms() { // LEHRFORMEN
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals(
                        "Vorlesung, Uebung",
                        module.getLecturingForms()
                )
        );
    }

    @Test
    void shouldExtractLecturingMethods() { // LEHRFORMEN
        Module module = extractionManager.extractModuleContent(pageContent);

        assertAll(
                () -> assertNotNull(module),
                () -> assertEquals(
                        "Lehrvortrag, Diskussion, Gruppenarbeit",
                        module.getLecturingMethods()
                )
        );
    }

}