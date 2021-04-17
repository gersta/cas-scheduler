package de.gerritstapper.casscheduler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import de.gerritstapper.casscheduler.daos.BlockDao;
import de.gerritstapper.casscheduler.daos.LectureDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
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

        jsonFileUtil = new JsonFileUtil(mapper);

        lectures = Arrays.asList(
            LectureDao.builder()
                .name("Name One")
                .blocks(Collections.singletonList(
                        BlockDao.builder()
                                .blockStart(LocalDate.of(2020, 10, 10))
                                .blockEnd(LocalDate.of(2020, 10, 11))
                                .location("S")
                                .build()
                ))
                .build(),
            LectureDao.builder()
                .name("Name Two")
                .blocks(Collections.singletonList(
                        BlockDao.builder()
                                .blockStart(LocalDate.of(2020, 12, 24))
                                .blockEnd(LocalDate.of(2020, 12, 25))
                                .location("MA")
                                .build()
                ))
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
                () -> assertTrue(json.contains("\"location\":\"MA\"")),
                () -> assertTrue(json.contains("\"location\":\"S\""))
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
        jsonFileUtil.serializeToFile(lectures, filename);

        assertTrue(new File(filename).exists());
    }
}
