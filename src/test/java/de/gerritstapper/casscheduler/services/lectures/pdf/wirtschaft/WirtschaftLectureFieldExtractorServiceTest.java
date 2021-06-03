package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WirtschaftLectureFieldExtractorServiceTest {

    private WirtschaftLectureFieldExtractorService extractorService;

    @BeforeEach
    void beforeEach() {
        extractorService = new WirtschaftLectureFieldExtractorService();
    }

    /**
     * ID
     */
    @Test
    void shouldReturnLectureIdWithContentIncludingTrailingDot() {
        String content = "W3M10001.";

        String result = extractorService.getId(content);

        assertEquals("W3M10001", result);
    }

    @Test
    void shouldReturnLectureIdWithContentIncludingTrailingWhitespace() {
        String content = "W3M10001 ";

        String result = extractorService.getId(content);

        assertEquals("W3M10001", result);
    }

    @Test
    void shouldReturnLectureIdWithContentIncludingTrailingLetter() {
        String content = "W3M10001a";

        String result = extractorService.getId(content);

        assertEquals("W3M10001", result);
    }

    @Test
    void shouldReturnLectureIdWithContentIncludingLeadingWhitespace() {
        String content = " W3M10001";

        String result = extractorService.getId(content);

        assertEquals("W3M10001", result);
    }

    @Test
    void shouldReturnLectureIdForLectureIdWithTooManyCharacters() {
        String content = "W3M100013D";

        String result = extractorService.getId(content);

        assertEquals("W3M10001", result);
    }

    /**
     * Name
     */
    @Test
    void shouldReturnLectureNameWithoutLeadingWhitespace() {
        String content = " GM III: Managerial Economics und Recht (HdH)";

        String result = extractorService.getName(content);

        assertEquals("GM III: Managerial Economics und Recht (HdH)", result);
    }

    @Test
    void shouldReturnLectureNameWithoutTrailingWhitespace() {
        String content = "GM III: Managerial Economics und Recht (HdH) ";

        String result = extractorService.getName(content);

        assertEquals("GM III: Managerial Economics und Recht (HdH)", result);
    }

    /**
     * Start
     */
    @Test
    void shouldReturnStartDateWithTrailingDot() {
        String content = "08.10.";

        String result = extractorService.getStart(content);

        assertEquals("08.10.", result);
    }

    @Test
    void shouldReturnStartWithoutTrailingWhitespace() {
        String content = "08.10. ";

        String result = extractorService.getStart(content);

        assertEquals("08.10.", result);
    }

    @Test
    void shouldReturnStartWithoutLeadingWhitespace() {
        String content = " 08.10.";

        String result = extractorService.getStart(content);

        assertEquals("08.10.", result);
    }

    @Test
    void shouldReturnStartWithoutLeadingCharacter() {
        String content = ")08.10.";

        String result = extractorService.getStart(content);

        assertEquals("08.10.", result);
    }

    /**
     * End
     */
    @Test
    void shouldReturnEndDateWithoutTrailingDot() {
        String content = "08.10.2020";

        String result = extractorService.getEnd(content);

        assertEquals("08.10.2020", result);
    }

    @Test
    void shouldReturnEndWithoutTrailingWhitespace() {
        String content = "08.10.2020";

        String result = extractorService.getEnd(content);

        assertEquals("08.10.2020", result);
    }

    @Test
    void shouldReturnEndWithoutLeadingWhitespace() {
        String content = " 08.10.2020";

        String result = extractorService.getEnd(content);

        assertEquals("08.10.2020", result);
    }

    @Test
    void shouldReturnEndWithoutLeadingCharacter() {
        String content = ")08.10.2020";

        String result = extractorService.getEnd(content);

        assertEquals("08.10.2020", result);
    }

    @Test
    void shouldReturnEndWithoutTrailingCharacter() {
        String content = "08.10.2020(";

        String result = extractorService.getEnd(content);

        assertEquals("08.10.2020", result);
    }

    /**
     * Location
     */
    @Test
    void shouldReturnLocationWithoutLeadingParantheses() {
        String content = "(RV";

        String result = extractorService.getLocation(content);

        assertEquals("RV", result);
    }

    @Test
    void shouldReturnLocationWithoutTrailingParantheses() {
        String content = "RV)";

        String result = extractorService.getLocation(content);

        assertEquals("RV", result);
    }

    @Test
    void shouldReturnLocationWithoutLeadingWhitespace() {
        String content = " RV";

        String result = extractorService.getLocation(content);

        assertEquals("RV", result);
    }

    @Test
    void shouldReturnLocationWithoutTrailingWhitespace() {
        String content = "RV ";

        String result = extractorService.getLocation(content);

        assertEquals("RV", result);
    }

    @Test
    void shouldReturnLocationWithGermanUppercaseÖ() {
        String content = "(LÖ)";

        String result = extractorService.getLocation(content);

        assertEquals("LÖ", result);
    }

    @Test
    void shouldReturnLocationWithGermanLowercaseÖ() {
        String content = "(Lö)";

        String result = extractorService.getLocation(content);

        assertEquals("Lö", result);
    }

    @Test
    void shouldReturnLocationWithGermanUppercaseÜ() {
        String content = "(LÜ)";

        String result = extractorService.getLocation(content);

        assertEquals("LÜ", result);
    }

    @Test
    void shouldReturnLocationWithGermanLowercaseÜ() {
        String content = "(Lü)";

        String result = extractorService.getLocation(content);

        assertEquals("Lü", result);
    }

    @Test
    void shouldReturnLocationWithGermanUppercaseÄ() {
        String content = "(LÄ)";

        String result = extractorService.getLocation(content);

        assertEquals("LÄ", result);
    }

    @Test
    void shouldReturnLocationWithGermanLowercaseÄ() {
        String content = "(Lä)";

        String result = extractorService.getLocation(content);

        assertEquals("Lä", result);
    }

}