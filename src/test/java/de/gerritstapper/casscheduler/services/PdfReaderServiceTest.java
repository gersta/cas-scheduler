package de.gerritstapper.casscheduler.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.gerritstapper.casscheduler.models.Lecture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PdfReaderServiceTest {

    public PdfReaderService service;
    public List<Lecture> lectures;

    @BeforeEach
    void beforeEach() throws IOException {
        service = new PdfReaderService();
        lectures = service.readPdf();
    }
    
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
                () -> assertTrue(placesOne.stream().allMatch(place -> !place.matches("[0-9]+"))),
                () -> assertTrue(placesTwo.stream().allMatch(place -> !place.matches("[0-9]+")))
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
    public void shouldNotIncludeParanthesesInPlaces() {
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
        List<String> names = lectures.stream()
                .map(lecture -> lecture.getName())
                .collect(Collectors.toList());

        assertAll(
                () -> assertTrue(names.stream().allMatch(name -> !name.isEmpty() && !name.isBlank())),
                () -> assertTrue(names.stream().allMatch(name -> !name.matches("[0-9]+")))
        );
    }

    @Test
    public void shouldNotReturnEmptyDates() {
        assertAll(
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartOne().isEmpty() && !lecture.getStartOne().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndOne().isEmpty() && !lecture.getEndOne().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartTwo().isEmpty() && !lecture.getStartTwo().isEmpty())),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndTwo().isEmpty() && !lecture.getEndTwo().isEmpty()))
        );
    }

    @Test
    public void shouldNotContainHyphonsInDates() {
        assertAll(
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartOne().contains("-"))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndOne().contains("-"))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartTwo().contains("-"))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndTwo().contains("-")))
        );
    }

    @Test
    public void shouldOnlyContainStrippedStrings() {
        assertAll(
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartOne().contains(" "))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndOne().contains(" "))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getStartTwo().contains(" "))),
                () -> assertTrue(lectures.stream().allMatch(lecture -> !lecture.getEndTwo().contains(" ")))
        );
    }
}
