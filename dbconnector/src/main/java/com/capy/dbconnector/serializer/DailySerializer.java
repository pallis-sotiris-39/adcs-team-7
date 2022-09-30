package com.capy.dbconnector.serializer;

import com.capy.dbconnector.objects.DailyModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DailySerializer implements Serializer<DailyModel> {

    public DailySerializer() {}

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, DailyModel data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + data);
            return null;
        }
    }

    @Override
    public byte[] serialize(String s, DailyModel dailyModel) {
        try {
            return objectMapper.writeValueAsBytes(dailyModel);
        } catch (JsonProcessingException e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + dailyModel);
            return null;
        }
    }
}
