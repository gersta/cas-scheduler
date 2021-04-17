package de.gerritstapper.casscheduler.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class JsonFileUtil {

    private final ObjectMapper mapper;

    public JsonFileUtil(
            final ObjectMapper mapper
    ) {
        this.mapper = mapper;
    }

    @SneakyThrows
    public String serialize(List<?> entities) {
        return mapper.writeValueAsString(entities);
    }

    @SneakyThrows
    public void serializeToFile(List<?> entities, String filename) {
        cleanUp(filename);

        mapper.writeValue(
                new File(filename),
                entities
        );
    }

    private void cleanUp(String filename) {
        if ( new File(filename).exists() ) {
            new File(filename).delete();
        }
    }

    @SneakyThrows
    public <T> T deserialize(String json, TypeReference<T> type) {
        return mapper.readValue(json, type);
    }
}
