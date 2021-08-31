package com.williammacedo.bookstoremanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;

public class JsonConversionUtils {
    public static String asJsonString(AuthorDTO expectedCreatedAuthorDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            return objectMapper.writeValueAsString(expectedCreatedAuthorDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
