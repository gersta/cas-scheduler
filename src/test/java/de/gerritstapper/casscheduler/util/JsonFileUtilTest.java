package de.gerritstapper.casscheduler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import de.gerritstapper.casscheduler.daos.LectureDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonFileUtilTest {

    private JsonFileUtil jsonFileUtil;
    private List<LectureDao> lectures;

    private String filename;

    @BeforeEach
    void beforeEach() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        filename = "src/test/resources/lectures.json";

        jsonFileUtil = new JsonFileUtil(mapper, filename);

        lectures = Arrays.asList(
            LectureDao.builder()
                .name("Name One")
                .firstBlockStart(LocalDate.of(2020, 10, 10))
                .firstBlockEnd(LocalDate.of(2020, 10, 11))
                .firstBlockLocation("S")
                .build(),
            LectureDao.builder()
                .name("Name Two")
                .firstBlockStart(LocalDate.of(2020, 12, 24))
                .firstBlockEnd(LocalDate.of(2020, 12, 25))
                .firstBlockLocation("MA")
                .build()
        );
    }

    @AfterEach
    void afterEach() {
        // clean up
        if ( new File(filename).exists() ) {
            new File(filename).delete();
        }
    }

    @Test
    void shouldSerializeEntitiesCorrectly() {
        String json = jsonFileUtil.serialize(lectures);

        assertAll(
                () -> assertTrue(json.contains("\"name\":\"Name One\"")),
                () -> assertTrue(json.contains("\"name\":\"Name Two\"")),
                () -> assertTrue(json.contains("\"firstBlockLocation\":\"MA\"")),
                () -> assertTrue(json.contains("\"firstBlockLocation\":\"S\""))
        );
    }

    @Test
    void shouldSerializeDatesCorrectly() {
        String json = jsonFileUtil.serialize(lectures);

        assertAll(
                () -> assertTrue(json.contains("2020-10-10")),
                () -> assertTrue(json.contains("2020-10-11")),
                () -> assertTrue(json.contains("2020-12-24")),
                () -> assertTrue(json.contains("2020-12-25"))
        );
    }

    @Test
    void shouldCreateJsonFile() {
        jsonFileUtil.serializeToFile(lectures);

        assertTrue(new File(filename).exists());
    }
}
