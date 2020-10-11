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
    private final String outputFileName;

    public JsonFileUtil(
            final ObjectMapper mapper,
            @Value("${cas-scheduler.json.output.lectures}") String outputFileName
    ) {
        this.mapper = mapper;
        this.outputFileName = outputFileName;
    }

    @SneakyThrows
    public String serialize(List<?> entities) {
        return mapper.writeValueAsString(entities);
    }

    @SneakyThrows
    public void serializeToFile(List<?> entities) {
        mapper.writeValue(
                new File(outputFileName),
                entities
        );
    }

    @SneakyThrows
    public <T> T deserialize(String json, TypeReference<T> type) {
        return mapper.readValue(json, type);
    }
}
