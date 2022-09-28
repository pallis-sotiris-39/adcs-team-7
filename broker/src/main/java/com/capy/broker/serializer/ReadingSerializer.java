package com.capy.broker.serializer;

import com.capy.broker.objects.ReadingModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ReadingSerializer implements Serializer<ReadingModel> {

    public ReadingSerializer () {}

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, ReadingModel data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + data);
            return null;
        }
    }

    @Override
    public byte[] serialize(String s, ReadingModel readingModel) {
        try {
            return objectMapper.writeValueAsBytes(readingModel);
        } catch (JsonProcessingException e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + readingModel);
            return null;
        }
    }
}
