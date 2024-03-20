package com.example.onboardingservice.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jooq.JSONB;

@UtilityClass
public class JooqUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @SneakyThrows
    public JSONB toJsonb(Object object) {
        return JSONB.jsonb(MAPPER.writeValueAsString(object));
    }

    @SneakyThrows
    public <T> T fromJsonb(JSONB jsonb, TypeReference<T> typeReference) {
        return MAPPER.readValue(jsonb.data(), typeReference);
    }

}
