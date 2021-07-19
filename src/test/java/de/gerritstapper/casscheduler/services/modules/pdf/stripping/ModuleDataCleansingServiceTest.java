package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModuleDataCleansingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleDataCleansingServiceTest {

    private ModuleDataCleansingService cleansingService;

    @BeforeEach
    void beforeEach() {
        cleansingService = new ModuleDataCleansingService();
    }

    @Test
    void shouldReplaceUppercaseÜwithUe() {
        String input = "Übung";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("Uebung", result);
    }

    @Test
    void shouldReplaceLowercaseÜwithUe() {
        String input = "übung";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("uebung", result);
    }

    @Test
    void shouldReplaceUppercaseÄwithAe() {
        String input = "Ärger";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("Aerger", result);
    }

    @Test
    void shouldReplaceLowercaseÄwithAe() {
        String input = "ärger";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("aerger", result);
    }

    @Test
    void shouldReplaceUppercaseÖwithOe() {
        String input = "Ödland";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("Oedland", result);
    }

    @Test
    void shouldReplaceLowercaseÖwithOe() {
        String input = "ödland";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("oedland", result);
    }

    @Test
    void shouldReplaceßWithSs() {
        String input = "Scheiße";

        String result = cleansingService.removeGermanUmlaute(input);

        assertEquals("Scheisse", result);
    }

}