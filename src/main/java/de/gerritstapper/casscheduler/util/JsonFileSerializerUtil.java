package de.gerritstapper.casscheduler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

public class JsonFileSerializerUtil {

    @SneakyThrows
    public static void serialize(List<?> entities) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(
                new File("src/main/resources/lectures.json"),
                entities
        );
    }
}
