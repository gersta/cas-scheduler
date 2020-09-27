package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.models.Lecture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorServiceTest {

    private static PdfReaderService service;
    private static List<Lecture> lectures;

    private static final String FILENAME = "M_T_Test_All_Pages.pdf";

    @BeforeAll
    static void beforeAll() throws IOException {
        service = new PdfReaderService(FILENAME);
        lectures = service.readPdf(null);

        service.removeRegions();
        service.closeDocument();
    }

    @Test
    public void shouldNotContainNullValues() {
        List<Object> nulls = lectures.stream()
                .filter(obj -> Objects.isNull(obj))
                .collect(Collectors.toList());

        assertTrue(nulls.isEmpty());
    }

    // ID

    @Test
    public void shouldNotReturnEmptyIds() {
        List<String> ids = lectures.stream()
                .map(lecture -> lecture.getId())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(ids.stream().allMatch(id -> !id.isEmpty() && !id.isBlank()))
        );
    }

    @Test
    public void shouldReturnIdStartingWithT3orW3() {
        List<String> ids = lectures.stream()
                .map(lecture -> lecture.getId())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(ids.stream().allMatch(id -> !id.isEmpty() && !id.isBlank())),
                () -> assertTrue(ids.stream().allMatch(id -> id.startsWith("T3M") || id.startsWith("W3M")))
        );
    }

    @Test
    public void shouldReturnIdsWith8Characters() {
        List<String> ids = lectures.stream()
                .map(lecture -> lecture.getId())
                .collect(Collectors.toList());

        assertTrue(ids.stream().allMatch(id -> id.length() == 8));
    }


    // NAME

    @Test
    public void shouldNotContainWhitespacesInNames() {
        List<String> names = lectures.stream()
                .map(lecture -> lecture.getName())
                .collect(Collectors.toList());

        // each name should be stripped
        names.forEach(name -> assertEquals(name.strip(), name));
    }

    @Test
    public void shouldStartNameWithCapitalLetter() {
        List<Character> firstChars = lectures.stream()
                .map(lecture -> lecture.getName())
                .map(name -> name.toCharArray())
                .map(chars -> chars[0])
                .collect(Collectors.toList());

        // some lectures start with a digit as the first character, like 3D-Technologien
        assertTrue(firstChars.stream().allMatch(character -> Character.isUpperCase(character) || Character.isDigit(character)));
    }

    @Test
    public void shouldNotReturnEmptyNames() {
        List<String> names = lectures.stream()
                .map(lecture -> lecture.getName())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(names.stream().allMatch(name -> !name.isEmpty() && !name.isBlank()))
        );
    }


    // PLACES

    @Test
    public void shouldOnlyReturnLettersOrQuestionMarkForLocations() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getLocationOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getLocationTwo())
                .collect(Collectors.toList());

        // should return MA instead of (MA)
        assertAll(
                // TODO: remove the check for lowercase letters and instead cleanse the content beforehand
                () -> assertTrue(placesOne.stream().allMatch(place -> place.matches("([A-Za-z]{1,3}|\\?)"))),
                () -> assertTrue(placesTwo.stream().allMatch(place -> place.matches("([A-Za-z]{1,3}|\\?)")))
        );
    }

    @Test
    public void shouldNotReturnEmptyLocations() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getLocationOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getLocationTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> !place.isEmpty() && !place.isBlank())),
                () -> assertTrue(placesTwo.stream().allMatch(place -> !place.isEmpty() && !place.isBlank()))
        );
    }

    @Test
    public void shouldNotIncludeParenthesesInLocations() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getLocationOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getLocationTwo())
                .collect(Collectors.toList());

        // should return MA instead of (MA)
        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> !place.isEmpty() && !place.isBlank())),
                () -> assertTrue(placesTwo.stream().allMatch(place -> !place.isEmpty() && !place.isBlank())),
                () -> assertTrue(placesOne.stream().allMatch(place -> !place.contains("(") && !place.contains(")"))),
                () -> assertTrue(placesTwo.stream().allMatch(place -> !place.contains("(") && !place.contains(")")))
        );
    }

    @Test
    public void shouldReturnLocationsWithMaximum3Characters() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getLocationOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getLocationTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> place.length() > 0 && place.length() <= 3)),
                () -> assertTrue(placesTwo.stream().allMatch(place -> place.length() > 0 && place.length() <= 3))
        );
    }

    @Test
    public void shouldReturnOnlyStrippedLocations() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getLocationOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getLocationTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> place.equals(place.strip() ))),
                () -> assertTrue(placesTwo.stream().allMatch(place -> place.equals(place.strip() )))
        );
    }

    // DATES

    @Test
    public void shouldNotReturnEmptyDates() {
        assertAll(
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartOne().isBlank() && !lecture.getStartOne().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndOne().isBlank() && !lecture.getEndOne().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartTwo().isBlank() && !lecture.getStartTwo().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndTwo().isBlank() && !lecture.getEndTwo().isEmpty()))
        );
    }

    @Test
    public void shouldNotContainHyphonsInDates() {
        assertAll(
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getStartOne().contains("-"))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getEndOne().contains("-"))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getStartTwo().contains("-"))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getEndTwo().contains("-")))
        );
    }

    @Test
    public void shouldOnlyContainStrippedStrings() {
        assertAll(
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getStartOne().contains(" "))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getEndOne().contains(" "))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getStartTwo().contains(" "))),
                () -> assertTrue(lectures.stream().noneMatch(lecture -> lecture.getEndTwo().contains(" ")))
        );
    }

    @Test
    public void shouldReturnStartsWith6Characters() {
        List<String> startsOne = lectures.stream()
                .map(lecture -> lecture.getStartOne())
                .collect(Collectors.toList());

        List<String> startsTwo = lectures.stream()
                .map(lecture -> lecture.getStartTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertFalse(startsOne.isEmpty()),
                () -> assertFalse(startsTwo.isEmpty()),
                () -> assertTrue(startsOne.stream().allMatch(start -> start.length() == 6)),
                () -> assertTrue(startsTwo.stream().allMatch(start -> start.length() == 6))
        );
    }

    @Test
    public void shouldReturnEndsWith10Characters() {
        List<String> endsOne = lectures.stream()
                .map(lecture -> lecture.getEndOne())
                .collect(Collectors.toList());

        List<String> endsTwo = lectures.stream()
                .map(lecture -> lecture.getEndTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(endsOne.stream().allMatch(end -> end.length() == 10)),
                () -> assertTrue(endsTwo.stream().allMatch(end -> end.length() == 10))
        );
    }
}
