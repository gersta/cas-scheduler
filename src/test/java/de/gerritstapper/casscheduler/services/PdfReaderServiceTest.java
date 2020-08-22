package de.gerritstapper.casscheduler.services;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.models.Lecture;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PdfReaderServiceTest {

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

    @BeforeEach
    void beforeEach() throws IOException {
        service = new PdfReaderService(FILENAME);
    }

    @AfterEach
    void afterEach() throws IOException {
        service.removeRegions();
        service.closeDocument();
    }

    @Test
    public void shouldReturn57LectureObjectsForPageOne() throws IOException {
        // create another local copy to not overwrite the global list
        List<Lecture> lectures = service.readPdf(0);
        lectures.forEach(lecture -> System.out.println(lecture));
        assertEquals(57, lectures.size());
    }

    @Test
    public void shouldReturn66LectureObjectsForPageTwo() throws IOException {
        List<Lecture> lectures = service.readPdf(1);
        lectures.forEach(lecture -> System.out.println(lecture));
        assertEquals(66, lectures.size());
    }

    @Test
    public void shouldReturn30LectureObjectsForPageThree() throws IOException {
        List<Lecture> lectures = service.readPdf(2);
        // TODO: decide whether to consider lectures that do not have dates and places, but rather a note
        assertEquals(30, lectures.size());
    }

    @Test
    public void shouldReturn58LectureObjectsForPageFour() throws IOException {
        List<Lecture> lectures = service.readPdf(3);
        lectures.forEach(lecture -> System.out.println(lecture));
        assertEquals(58, lectures.size());
    }

    @Test
    public void shouldReturn64LectureObjectsForPageFive() throws IOException {
        List<Lecture> lectures = service.readPdf(4);
        assertEquals(64, lectures.size());
    }

    @Test
    public void shouldReturn276LectureObjectsForAllPages() throws IOException {
        List<Lecture> lectures = service.readPdf(null);

        assertEquals(276, lectures.size());
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

        assertTrue(firstChars.stream().allMatch(character -> Character.isUpperCase(character)));
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

    @Test
    public void shouldReturnOnlyLettersForName() {
        Pattern pattern = Pattern.compile("\\W");

        List<String> names = lectures.stream()
                .map(lecture -> lecture.getName())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(names.stream().allMatch(name -> !name.isEmpty() && !name.isBlank())),
                () -> assertTrue(names.stream().noneMatch(name -> pattern.matcher(name).find()))
        );
    }


    // PLACES

    @Test
    public void shouldOnlyReturnLettersForPlaces() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getPlaceOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getPlaceTwo())
                .collect(Collectors.toList());

        // should return MA instead of (MA)
        assertAll(
                () -> assertTrue(placesOne.stream().noneMatch(place -> place.matches("[0-9]+"))),
                () -> assertTrue(placesTwo.stream().noneMatch(place -> place.matches("[0-9]+")))
        );
    }

    @Test
    public void shouldNotReturnEmptyPlaces() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getPlaceOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getPlaceTwo())
                .collect(Collectors.toList());

        // should return MA instead of (MA)
        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> !place.isEmpty() && !place.isBlank())),
                () -> assertTrue(placesTwo.stream().allMatch(place -> !place.isEmpty() && !place.isBlank()))
        );
    }

    @Test
    public void shouldNotIncludeParenthesesInPlaces() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getPlaceOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getPlaceTwo())
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
    public void shouldReturnPlacesWithOneOrTwoCharacters() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getPlaceOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getPlaceTwo())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(placesOne.stream().allMatch(place -> place.length() > 0 && place.length() <= 3)),
                () -> assertTrue(placesTwo.stream().allMatch(place -> place.length() > 0 && place.length() <= 3))
        );
    }

    @Test
    public void shouldReturnOnlyStrippedPlaces() {
        List<String> placesOne = lectures.stream()
                .map(lecture -> lecture.getPlaceOne())
                .collect(Collectors.toList());

        List<String> placesTwo = lectures.stream()
                .map(lecture -> lecture.getPlaceTwo())
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
